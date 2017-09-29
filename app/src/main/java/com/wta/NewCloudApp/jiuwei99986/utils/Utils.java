package com.wta.NewCloudApp.jiuwei99986.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.wta.NewCloudApp.jiuwei99986.App;
import com.wta.NewCloudApp.jiuwei99986.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Utils utils;
    private HashMap<String, String> emojiMap = new HashMap<String, String>();
    private HashMap<String, String> sortMap = new HashMap<String, String>();

    public static Utils getInstace() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    public static String savePhoto(Bitmap photoBitmap, String path, String photoName) {
        String localPath = null;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File photoFile = new File(path, photoName + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                        fileOutputStream)) { // 转换完成
                    localPath = photoFile.getPath();

                    fileOutputStream.flush();
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            localPath = null;
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            localPath = null;
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return localPath;
    }
    public String parseSort(String fid){
        return sortMap.get(fid);
    }
    public void initSort(){
        sortMap.clear();
        String[] number = App.getContext().getResources().getStringArray(R.array.sortNumber);
        String[] sort = App.getContext().getResources().getStringArray(R.array.sort);
        for (int i = 0; i < number.length; i++) {
            sortMap.put(number[i],sort[i]);
        }
    }
    public List<String> getEmojiFile(Context context) {
        String[] emojis = context.getResources().getStringArray(R.array.emoji);
        List<String> list = Arrays.asList(emojis);
        return list;
    }

    public void getChatMap(Context context) {
        for (String str : getEmojiFile(context)) {
            String[] text = str.split(",");
            String fileName = text[0]
                    .substring(0, text[0].lastIndexOf("."));
            emojiMap.put(text[1], fileName);
        }
    }
    private static String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }


    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public static void showLongToast(Context context, int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG).show();
    }

    public SpannableString getExpressionString(Context context, String str) {
        getChatMap(context);
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
//		String zhengze = "\\[[^\\]]+\\]";
//		String zhengze = "[\u4e00-\u9fa5]";
        //String zhengze="/:[\\x21-\\x2E\\x30-\\x7E]{1,8}";
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
        }
        return spannableString;
    }

    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
//		 String input = "你好什么人/::P你好啊~啊啊啊/::D";
        Matcher matcher = patten.matcher(spannableString);
        /*View view = LayoutInflater.from(context).inflate(R.layout.chat_image, null);
        ImageView imageView= (ImageView) view.findViewById(R.id.chatEmojy);*/
//		System.out.println("matcher.find()+》》》》》》》》》》》》》》》"+matcher.find());
        while (matcher.find()) {
            String key = matcher.group();

            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
//			System.out.println("key+,,,,,,,,,,,,,"+key);
//			System.out.println("matcher.find()+,,,,,,,,,,,,,"+matcher.find());
            if (matcher.start() < start) {
                continue;
            }
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            if (resId == 0) {
                resId = context.getResources().getIdentifier("emoji_0",
                        "drawable", context.getPackageName());
            }
            // 通过上面匹配得到的字符串来生成图片资源id
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
                //bitmap.
                bitmap = Bitmap.createScaledBitmap(bitmap, dip2px(context, 35), dip2px(context, 35), true);
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(context, resId);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
//				System.out.println("------------"+res/Id);
//				int end = matcher.start() + key.length();
                int end = matcher.start() + key.length();
//				System.out.println("+++++++++++++"+resId);
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static long getDateTime(String date) {
        Calendar c = Calendar.getInstance();
        long time = 0;
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
            time = c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long getMinuteDateTime(String date) {
        Calendar c = Calendar.getInstance();
        long time = 0;
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date));
            time = c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0;i < arr.length;i ++) {
            String param = arr[i];
            try{
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                       /* if (QLog.isColorLevel()) {
                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
                        }*/
                        break;
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }
}