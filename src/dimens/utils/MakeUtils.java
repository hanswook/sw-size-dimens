package dimens.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import dimens.constants.DimenTypes;


public class MakeUtils {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
    private static final String XML_RESOURCE_START = "<resources>\r\n";
    private static final String XML_RESOURCE_END = "</resources>\r\n";
    private static final String XML_DIMEN_TEMPLETE = "<dimen name=\"qb_%1$spx_%2$d\">%3$.2fdp</dimen>\r\n";

    private static final String XML_DIMEN_TEMPLETE_SP = "<dimen name=\"ws%1$s%2$dsp\">%3$.2fsp</dimen>\r\n";
    private static final String XML_DIMEN_TEMPLETE_SP_5 = "<dimen name=\"ws%1$s%2$d_5sp\">%3$.2fsp</dimen>\r\n";
    private static final String XML_DIMEN_TEMPLETE_DP = "<dimen name=\"ws%1$s%2$ddp\">%3$.2fdp</dimen>\r\n";
    private static final String XML_DIMEN_TEMPLETE_DP_5 = "<dimen name=\"ws%1$s%2$d_5dp\">%3$.2fdp</dimen>\r\n";


    private static final String XML_BASE_DPI = "<dimen name=\"base_dpi\">%ddp</dimen>\r\n";
    private static final int MAX_SIZE = 400;

    /**
     * 生成的文件名
     */
    private static final String XML_NAME = "dimens.xml";


    public static float px2dip(float pxValue, int sw, int designWidth) {
        float dpValue = (pxValue / (float) designWidth) * sw;
        BigDecimal bigDecimal = new BigDecimal(dpValue);
        float finDp = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return finDp;
    }

//    <dimen name="ws0sp">0.00sp</dimen>
//    <dimen name="ws0_5sp">0.56sp</dimen>
//    <dimen name="ws1sp">1.11sp</dimen>


    /**
     * 生成所有的尺寸数据
     *
     * @param type
     * @return
     */
    private static String makeAllDimens(DimenTypes type, int designWidth) {
        float dpValue;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(XML_HEADER);
            sb.append(XML_RESOURCE_START);
            //备份生成的相关信息
            temp = String.format(XML_BASE_DPI, type.getSwWidthDp());
            sb.append(temp);


            for (double i = 0; i <= 40; i = i + 0.5) {
                dpValue = px2dip((float) i, type.getSwWidthDp(), designWidth);
                if (i % 1 != 0) {
                    temp = String.format(XML_DIMEN_TEMPLETE_SP_5, "", (int) i, dpValue);
                } else {
                    temp = String.format(XML_DIMEN_TEMPLETE_SP, "", (int) i, dpValue);
                }
                sb.append(temp);
            }
            sb.append("\n\n\n\n");

            for (double i = 0; i <= MAX_SIZE; i = i + 0.5) {

                dpValue = px2dip((float) i, type.getSwWidthDp(), designWidth);
                if (i % 1 != 0) {
                    temp = String.format(XML_DIMEN_TEMPLETE_DP_5, "", (int) i, dpValue);
                } else {
                    temp = String.format(XML_DIMEN_TEMPLETE_DP, "", (int) i, dpValue);
                }

//                temp = String.format(XML_DIMEN_TEMPLETE, "", i, dpValue);
                sb.append(temp);
            }

            sb.append(XML_RESOURCE_END);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 生成的目标文件夹
     * 只需传宽进来就行
     *
     * @param type     枚举类型
     * @param buildDir 生成的目标文件夹
     */
    public static void makeAll(int designWidth, dimens.constants.DimenTypes type, String buildDir) {
        try {
            //生成规则
            final String folderName;
            if (type.getSwWidthDp() > 0) {
                //适配Android 3.2+
                folderName = "values-sw" + type.getSwWidthDp() + "dp";
            } else {
                return;
            }

            //生成目标目录
            File file = new File(buildDir + File.separator + folderName);
            if (!file.exists()) {
                file.mkdirs();
            }

            //生成values文件
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + File.separator + XML_NAME);
            fos.write(makeAllDimens(type, designWidth).getBytes());
            fos.flush();
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
