import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码工具
 */
public class QrCodeUtil {

    /**
     * 生成二维码
     * @param content 二维码内容
     * @param size 二维码大小
     * @return
     */
    public static BufferedImage generatorQrCode(String content, Integer size){
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //编码方式
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //纠错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            //二维码BufferedImage
            BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);

            return qrCode;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成带图片的二维码
     * @param content 二维码内容
     * @param size 二维码大小
     * @param logoFile 二维码logo
     * @return
     */
    public static BufferedImage generatorQrCode(String content, Integer size, File logoFile){
        try {
            //二维码BufferedImage
            BufferedImage qrCode = generatorQrCode(content, size);
            //logo BufferedImage
            BufferedImage logo = ImageIO.read(logoFile);
            //logo大小设置为二维码的1/5
            BufferedImage compressLogo = new BufferedImage(qrCode.getWidth() / 5,qrCode.getHeight() / 5, BufferedImage.TYPE_INT_ARGB);
            //创建画板
            Graphics2D logoGraphics = (Graphics2D) compressLogo.getGraphics();
            //设置画板的背景颜色为白色
            logoGraphics.setBackground(Color.WHITE);

            //AlphaComposite.SRC:源复制给目标
            logoGraphics.setComposite(AlphaComposite.Src);
            logoGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            logoGraphics.setColor(Color.WHITE);
            //填充一个30mm的圆
            logoGraphics.fill(new RoundRectangle2D.Float(0, 0, compressLogo.getWidth(), compressLogo.getHeight(), size / 10, size / 10));

            //AlphaComposite.SRC_ATOP:源和目标重叠的部分组合在目标上
            logoGraphics.setComposite(AlphaComposite.SrcAtop);
            //logo绘画到画板
            logoGraphics.drawImage(logo, 0,0,compressLogo.getWidth(), compressLogo.getHeight(),null);
            logoGraphics.dispose();

            //newQrCode,分别装二维码buffer和缩小后的Logo的buffer
            BufferedImage newQrCode = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D newQrCodeGraphics = (Graphics2D) newQrCode.getGraphics();
            //logo放置的坐标x
            int x = (qrCode.getWidth() - compressLogo.getWidth()) / 2;
            //logo放置的坐标y
            int y = (qrCode.getHeight() - compressLogo.getHeight()) / 2;
            newQrCodeGraphics.drawImage(qrCode, 0, 0, null);
            newQrCodeGraphics.drawImage(compressLogo, x, y, null);
            newQrCodeGraphics.dispose();

            return newQrCode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成带背景图的二维码
     * @param content 二维码内容
     * @param size 二维码大小
     * @param logoFile 二维码logo
     * @param backgroundFile 二维码背景
     * @return
     */
    public static BufferedImage generatorQrCode(String content, Integer size, File logoFile, File backgroundFile){
        try {
            BufferedImage qrCode = generatorQrCode(content, size, logoFile);
            BufferedImage background = ImageIO.read(backgroundFile);

            //装背景图buffer和二维码buffer
            Graphics2D  newQrCodeGraphics = (Graphics2D) background.getGraphics();
            //qrCode放置的坐标x
            int x = (background.getWidth() - qrCode.getWidth()) / 2;
            //qrCode放置的坐标y
            int y = (background.getHeight() - qrCode.getHeight()) / 2;
            //如果背景图小于二维码
            if(x < 0 || y < 0){
                throw new RuntimeException("背景图片小于二维码图片");
            }
            newQrCodeGraphics.drawImage(background, 0, 0, null);
            newQrCodeGraphics.drawImage(qrCode, x, y, null);
            newQrCodeGraphics.dispose();

            return background;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
