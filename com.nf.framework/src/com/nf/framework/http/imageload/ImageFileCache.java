package com.nf.framework.http.imageload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.nf.framework.ImageUtil;

public class ImageFileCache {
    private static final String WHOLESALE_CONV = ".cach";
    /**过期时间3天**/
    private static final long mTimeDiff = 3 * 24 * 60 * 60 * 1000;
    public ImageFileCache(String cachDir)
    {
        //清理文件缓存
        removeCache(getDirectory(cachDir));
    }
    
    public Bitmap getImage(float density,String url,String cachDir) {    
        final String path = getDirectory(cachDir) + "/" + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
        		Bitmap bmp=density!=0?ImageUtil.decodeBitmapFromFile(path, density):BitmapFactory.decodeFile(path);
            if(bmp==null)
            {
                file.delete();
            }else
            {
                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }
    
    /***缓存空间大小****/
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE=10;
    /**
     * 
     * @param bm
     * @param url
     * @param cachDir 缓存路径
     */
    public void saveBmpToSd(Bitmap bm, String url,String cachDir) {
        if (bm == null) {        
        //需要保存的是一个空值
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE >freeSpaceOnSd()) {
        //SD空间不足
        return;
        }
        String filename =convertUrlToFileName(url);
        String dir = getDirectory(cachDir);
        File file = new File(dir +"/" + filename);
        try {
        file.createNewFile();
        OutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
       
        } catch (FileNotFoundException e) {
        Log.w("ImageFileCache","FileNotFoundException");
        } catch (IOException e) {
        Log.w("ImageFileCache","IOException");
        }
        bm=null;
        } 
    
    /**
	 * 创建绝对路径多个文件夹
	 * 
	 * @param fileName
	 *            要创建的文件名
	 * @return 创建得到的文件
	 */
	public void createAbsoluteDir(String dirName) {
		if(dirName==null||dirName.length()==1){
			return;
		}
		File dir = new File(dirName);
		dir.mkdirs();
	}
    private static final int CACHE_SIZE=10;
    
    // 清理缓存
    /**
     * 计算存储目录下的文件大小，
     * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * 那么删除40%最近没有被使用的文件
     * 
     * @param dirPath
     * @param filename
     */
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return false;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }

        if (dirSize > CACHE_SIZE * MB
                || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);

            Arrays.sort(files, new FileLastModifSort());

            Log.i("ImageFileCache", "清理缓存文件");
            for (int i = 0; i < removeFactor; i++) {

                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }

            }

        }

        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }
        return true;
    }

    /**
     * TODO 根据文件的最后修改时间进行排序 *
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    
    /**
     * 删除过期文件
     * 
     * @param dirPath
     * @param filename
     */
    public void removeExpiredCache(String dirPath, String filename) {

        File file = new File(dirPath, filename);

        if (System.currentTimeMillis() - file.lastModified() > mTimeDiff) {

            Log.i("ImageFileCache", "Clear some expiredcache files ");

            file.delete();
        }

    }
    
    /**
     * 修改文件的最后修改时间
     * 这里需要考虑,是否将使用的图片日期改为当前日期
     * @param path
     */
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }
    /**
    * 计算sdcard上的剩余空间
    * @return
    */
    private int MB=1024*1024;
    private int freeSpaceOnSd() {
    StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath());
    double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
    return (int) sdFreeMB;
    } 
    
  
    /**将url转成文件名**/
    private String convertUrlToFileName(String url) {
    		String[] strs = url.split("/");
    		String fileName=strs[strs.length - 1];
    		Pattern p = Pattern.compile("[\\s\\S]+.(gif|jpg|png|jpeg)$",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(fileName);
		if (m.matches()) {
			return fileName+WHOLESALE_CONV;
		}else{
			return url.substring("http://".length()) + WHOLESALE_CONV;
		}
    }
   
    /**获得缓存目录**/
    private String getDirectory(String cachdir) {
    	String dir = getSDPath() + "/" + cachdir;
        if(dir!=null){
        	createAbsoluteDir(dir);
        }
//        String substr = dir.substring(0, 4);
//        if (substr.equals("/mnt")) {
//            dir = dir.replace("/mnt", "");
//        }
        return dir;
    }
    /**** 取SD卡路径不带/ ****/
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        if(sdDir!=null)
        {
        return sdDir.toString();
        }else
        {
            return "";
        }
    }
    
}