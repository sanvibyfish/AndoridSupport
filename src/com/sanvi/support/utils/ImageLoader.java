package com.sanvi.support.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.sanvi.support.R;


/**
 * 图片加载
 * 缓存图片目录参数 imageCacheDirectory
 * 默认图片参数 stub_id
 * @author sanvi
 *
 */
public class ImageLoader {
	
	private static ImageLoader instance;
	private static String imageCacheDirectory = "/SANVI_SUPPORT_IMAGES";
	final int stub_id= R.drawable.ic_default_image;
	
	public static String getImageCacheDirectory() {
		return imageCacheDirectory;
	}

	public static void setImageCacheDirectory(String imageCacheDirectory) {
		ImageLoader.imageCacheDirectory = imageCacheDirectory;
	}

	public static ImageLoader getInstance(Context context){
		if(instance ==  null){
			instance = new ImageLoader(context);
		}
		return instance;
	}
	
	public static ImageLoader getInstance(Context context,String cacheDir){
		setImageCacheDirectory(cacheDir);
		return getInstance(context);
	}
    //the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Bitmap> cache=new HashMap<String, Bitmap>();
    
    private File cacheDir;
    
    public ImageLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),imageCacheDirectory);
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    
    public void displayImage(String url, ImageView imageView)
    {
        if(cache.containsKey(url))
            imageView.setImageBitmap(cache.get(url));
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }    
    }
        
    private void queuePhoto(String url, ImageView imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    
    private Bitmap getBitmap(String url) 
    {
    	System.out.println(url);
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        File f=new File(cacheDir, filename);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        PatchInputStream pis = null;
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
//            InputStream is = new URL(url).openStream();
            InputStream is = callWebservice(url);
            pis = new PatchInputStream(is);
            OutputStream os = new FileOutputStream(f);
            StreamUtils.CopyStream(pis, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }

    
    private InputStream callWebservice(String serviceURL) {
        HttpClient client=new DefaultHttpClient();
        HttpGet getRequest=new HttpGet();

           try {
             // construct a URI object
             getRequest.setURI(new URI(serviceURL));
              } catch (URISyntaxException e) {
                Log.e("URISyntaxException", e.toString());
               }

            // buffer reader to read the response
           BufferedReader in=null;
           // the service response
          HttpResponse response=null;
             try {
                  // execute the request
                  response = client.execute(getRequest);
               } catch (ClientProtocolException e) {
                 Log.e("ClientProtocolException", e.toString());
               } catch (IOException e) {
                 Log.e("IO exception", e.toString());
            }
      if(response!=null)
            try {
                return response.getEntity().getContent();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
        else
          return null;
        return null;

    }
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//           BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
//            //Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE=70;
//            int width_tmp=o.outWidth, height_tmp=o.outHeight;
//            int scale=1;
//            while(true){
//                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
//                    break;
//                width_tmp/=2;
//                height_tmp/=2;
//                scale++;
//            }
            
            //decode with inSampleSize
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, null);
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all instances of this ImageView
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url);
                        cache.put(photoToLoad.url, bmp);
                        if(((String)photoToLoad.imageView.getTag()).equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }
    
    PhotosLoader photoLoaderThread=new PhotosLoader();
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        //clear memory cache
        cache.clear();
        
        //clear SD cache
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }

}
