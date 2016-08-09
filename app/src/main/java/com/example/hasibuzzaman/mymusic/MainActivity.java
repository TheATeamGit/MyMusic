package com.example.hasibuzzaman.mymusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{
   MediaPlayer mp= new MediaPlayer();
    String sname;
    Button backB;
    Button goB;
    Button startB;
    Button prevB;
    Button nextB;
    SeekBar sb;
    TextView ct;
    TextView tt;
    TextView tsname;
    ArrayList<File> songsList;
    ArrayList<String> songsName;
    ListView lv;
    static MyMusic  Mmu;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsName =new ArrayList<String>();
        backB= (Button) findViewById(R.id.back);
        goB= (Button) findViewById(R.id.go);
        startB= (Button) findViewById(R.id.start);
        prevB= (Button) findViewById(R.id.prev);
        nextB= (Button) findViewById(R.id.next);
        lv= (ListView) findViewById(R.id.listView);

        ct= (TextView) findViewById(R.id.ctime);
        tt= (TextView) findViewById(R.id.ttime);
        tsname= (TextView) findViewById(R.id.songname);
        tsname.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tsname.setSelected(true);
        tsname.setSingleLine(true);


        sb= (SeekBar) findViewById(R.id.seekBar);

        songsList = getAllSong(Environment.getExternalStorageDirectory());

        lv.setAdapter(new MyAdapter(MainActivity.this,songsName));

        mp = MediaPlayer.create(MainActivity.this,Uri.parse(songsList.get(0).toString()));
        sname = songsName.get(0);
        // mp.start();


        backB.setOnClickListener(this);
        startB.setOnClickListener(this);
        goB.setOnClickListener(this);
        nextB.setOnClickListener(this);
        prevB.setOnClickListener(this);

       /* Thread ab=new Thread(new Mybar(mp,sb));
        ab.start();
*/


       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               mp.stop();
               mp.release();
        /*       if(Mmu!=null)
               {
                   Mmu.cancel(true);
               }*/

               if (Mmu != null && Mmu.getStatus() != AsyncTask.Status.FINISHED)
               {
                   Mmu.cancel(true);
                 }
               position=i;
               mp = MediaPlayer.create(MainActivity.this,Uri.parse(songsList.get(i).toString()));
               mp.start();
               startB.setText("||");
               sb.setMax(mp.getDuration());
                  // Log.e("PD DEBUG", mp.getCurrentPosition()+" :  Current Position");
                  sname = songsName.get(i);
                   Mmu=new MyMusic( mp,startB,  sb,  ct,  tt,  tsname,sname);
                   Mmu.execute();

           }
       });



       sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
           }
       });

    }  //   End Of Oncreate


   /* void startall(int i)
    {

        mp.pause();
        mp.release();
        if(Mmu!=null)
        {
            Mmu.cancel(true);
        }
        position=i;
        mp = MediaPlayer.create(MainActivity.this,Uri.parse(songsList.get(i).toString()));
        mp.start();
        startB.setText("||");
        Log.e("PD DEBUG", mp.getCurrentPosition()+" :  Current Position");
        sname = songsName.get(i);
        Mmu=new MyMusic( mp,startB,  sb,  ct,  tt,  tsname,sname);
        Mmu.execute();

    }*/


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.back :
                mp.seekTo(mp.getCurrentPosition()-5000);
                return;

            case R.id.start :
                if(mp.isPlaying())
                {
                    mp.pause();
                    startB.setText(">");
                }
                else
                {
                    mp.start();
                    startB.setText("||");
                    if(Mmu==null)
                    {

                        Mmu=new MyMusic( mp,startB,  sb,  ct,  tt,  tsname ,sname );
                        Mmu.execute();
                        sb.setMax(mp.getDuration());
                    }
                   /* Thread a= new Thread(new Mythread(mp,ct,tt));
                    a.start();*/




                }
                return;
            case R.id.go :
                mp.seekTo(mp.getCurrentPosition()+5000);
                return;


            case R.id.next :

                mp.stop();
                mp.release();

                /*       if(Mmu!=null)
               {
                   Mmu.cancel(true);
               }*/

                if (Mmu != null && Mmu.getStatus() != AsyncTask.Status.FINISHED)
                {
                    Mmu.cancel(true);
                }

                position=((position+1)%songsList.size());
                mp=mp.create(MainActivity.this,Uri.parse(songsList.get(position).toString()));
                mp.start();
                startB.setText("||");
                sb.setMax(mp.getDuration());
               // Log.e("PD DEBUG", mp.getCurrentPosition()+" :  Current Position");
                sname = songsName.get(position);
                Mmu=new MyMusic( mp,startB,  sb,  ct,  tt,  tsname,sname);
                Mmu.execute();

                return;

            case R.id.prev :

                mp.stop();
                mp.release();

               /*       if(Mmu!=null)
               {
                   Mmu.cancel(true);
               }*/

                if (Mmu != null && Mmu.getStatus() != AsyncTask.Status.FINISHED)
                {
                    Mmu.cancel(true);
                }

                if(position==0)
                {
                    position= songsList.size()-1;
                }
                else
                {
                    position=position-1;
                }
                mp=mp.create(MainActivity.this,Uri.parse(songsList.get(position).toString()));
                mp.start();
                startB.setText("||");
                sb.setMax(mp.getDuration());
                //Log.e("PD DEBUG", mp.getCurrentPosition()+" :  Current Position");
                sname = songsName.get(position);
                Mmu=new MyMusic( mp,startB,  sb,  ct,  tt,  tsname,sname);
                Mmu.execute();

                return;
        }
    }

    ArrayList<File> getAllSong(File root)
    {
        ArrayList<File> files=new ArrayList<>();
        File f[]=null;
        try {
            f= root.listFiles();
        }catch(NullPointerException e)
        {

        }
        for(File fy:f)
        {
          //  Log.e("PD DEBUG", " :  Inside Get AllSong");
            if(fy.isDirectory() && !fy.isHidden())
            {
                files.addAll(getAllSong(fy));
            }
            else if(fy.getName().toString().toLowerCase().endsWith(".mp3"))
            {
                files.add(fy);
                songsName.add(fy.getName().toString().replace(".mp3",""));
                //Toast.makeText(MainActivity.this,fy.getName().toString().toLowerCase(),Toast.LENGTH_LONG).show();
            }
        }
        return files;
    }
}



 // Synching The Time And ProgressBar

class MyMusic extends AsyncTask
{
    MediaPlayer mp= new MediaPlayer();
    Button startB;
    SeekBar sb;
    TextView ct;
    TextView tt;
    TextView tsname;
    String sname;
    private boolean running = true;
    public MyMusic(MediaPlayer mp, Button startB, SeekBar sb, TextView ct, TextView tt, TextView tsname,String sname) {
       this.sname=sname;

        this.mp = mp;
        this.startB = startB;
        this.sb = sb;
        this.ct = ct;
        this.tt = tt;
        this.tsname = tsname;
    }


    @Override
    protected void onCancelled() {
        //Toast.makeText(getApplicationContext(), "asynctack cancelled.....", Toast.LENGTH_SHORT).show();
        //dialog.hide(); /*hide the progressbar dialog here...*/
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sb.setMax(mp.getDuration());
        int l= mp.getDuration()/60000;
        tt.setText(l+" : "+ (mp.getDuration()/1000-(60*l))+"");
        tsname.setText(sname);



    }



    @Override
    protected void onProgressUpdate(Object[] values) {
        int value= (Integer) values[0];
        sb.setProgress(value);
        if(value/1000 >= 60)
        {   int l= value/60000;
            ct.setText(l+" : "+ (value/1000-(60*l))+"");
        }
        else if(value/1000>0)
        {
            ct.setText(" 00 :" +value/1000+"");
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {


            while(mp.getCurrentPosition()<=mp.getDuration()-1000)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                if (isCancelled())
                {

                    break; // don't forget to terminate this method
                }
                else
                {
                    try {
                        publishProgress(mp.getCurrentPosition());
                    } catch (IllegalStateException e) {

                    }

                }

            }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}  // End Of AsyncTask


/*


class Mythread implements Runnable{
    Handler h=new Handler();
    MediaPlayer mp= new MediaPlayer();
    TextView ct;
    TextView tt;

    public Mythread(MediaPlayer mp, TextView ct, TextView tt) {
        this.mp = mp;
        this.ct = ct;
        this.tt = tt;
    }

    @Override
    public void run() {
               while(mp.getCurrentPosition()<=mp.getDuration())
               {
                   h.post(new Thread(){
                       @Override
                       public void run() {
                           if(mp.getCurrentPosition()/1000 >= 60)
                           {   int l= mp.getCurrentPosition()/60000;
                               ct.setText(l+" : "+ (mp.getCurrentPosition()/1000-(60*l))+"");
                           }
                           else if(mp.getCurrentPosition()/1000>0)
                           {
                               ct.setText(" 00 :" + mp.getCurrentPosition()/1000+"");
                           }

                       }
                   });


                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

               }
    }
}



class Mybar implements Runnable{
    Handler h=new Handler();
    MediaPlayer mp;
    SeekBar sb;

    public Mybar( MediaPlayer mp, SeekBar sb) {
        this.mp = mp;
        this.sb = sb;
    }

    @Override
    public void run() {
        while(mp.getCurrentPosition()<=mp.getDuration())
        {
            h.post(new Thread(){
                @Override
                public void run() {
                   sb.setProgress(mp.getCurrentPosition());
                }
            });


            try {
                Thread.sleep(01);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

*/
