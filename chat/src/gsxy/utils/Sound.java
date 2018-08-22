package gsxy.utils;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sound {
    String path = new String("chat/sound/");//目录
    String file = new String("online.wav"); //播放的音乐文件名称
    boolean sign;//标志，true为正在播放，反之没有播放
    public Sound(){
        loadSound(); //构造方法里直接调用方法
    }

    private void loadSound() {
        try {
            FileInputStream fileau =new FileInputStream(path+file);
            AudioStream as =new AudioStream(fileau);
            AudioPlayer.player.start(as);
//            //关闭流 每次必可不少 在sound里面需要关掉。
//            fileau.close();
//            as.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
