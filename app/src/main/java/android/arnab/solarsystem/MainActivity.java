package android.arnab.solarsystem;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener
{

    public static int NO_OF_PLANETS=50;
    RelativeLayout layout;
    static float topMargin[]=new float[NO_OF_PLANETS+1];
    static float leftmargin[]=new float[NO_OF_PLANETS+1];
    static ImageButton planets[]=new ImageButton[NO_OF_PLANETS];
    float sunTop=0,sunLeft=0,screenWidth=0,screenHeight=0,sunCenterX=0,sunCenterY=0;
    static int currentX=0,currentY=0,startX=0,startY=0,sunWidth=150,sunHeight=150,CHK_IF_RESUME=0,SOLAR_SYSTEM_HEIGHT=3000,SOLAR_SYSTEM_WIDTH=3000
            ,ORBIT_JUMP=55,IDX=0;
    int pos=0;
    public static float d;
    static long startTime=0,endTime=0;
    ImageButton sun,halfSun,halfSunHori;
    DrawOrbit orbitRegion;
    static VideoView stars;
    Handler handler1 = new Handler();
    Handler handler2 = new Handler();
    Handler handler3 = new Handler();

    //int r = (int) (Math.random() * (upper - lower)) + lower;
    int strt=100;
    float[] quantizedOrbits=new float[(SOLAR_SYSTEM_HEIGHT/2-strt)/ORBIT_JUMP];

    int pics[]={R.drawable.mercury,R.drawable.venus,R.drawable.earth,R.drawable.mars,R.drawable.jupiter,R.drawable.saturn,R.drawable.uranus,R.drawable.uranus,R.drawable.neptune,R.drawable.pluto};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        d = this.getResources().getDisplayMetrics().density;

        for(int a=0;a<quantizedOrbits.length;a++)
        {
            quantizedOrbits[a]=strt+ORBIT_JUMP*(a+1)*d+5;

        }

        layout=(RelativeLayout)findViewById(R.id.scrollableSpace);
        orbitRegion=(DrawOrbit)findViewById(R.id.orbitsRegion);
        stars=(VideoView)findViewById(R.id.stars);
        halfSun=new ImageButton(this);
        halfSunHori=new ImageButton(this);


        halfSun.setVisibility(View.GONE);
        halfSun.setScaleType(ImageButton.ScaleType.FIT_XY);
        halfSun.setLayoutParams(new RelativeLayout.LayoutParams((int)((sunWidth/2)*d),(int)(sunHeight*d)));
        halfSun.setBackgroundColor(Color.TRANSPARENT);

        halfSunHori.setVisibility(View.GONE);
        halfSunHori.setScaleType(ImageButton.ScaleType.FIT_XY);
        halfSunHori.setLayoutParams(new RelativeLayout.LayoutParams((int)(sunWidth*d),(int)((sunHeight/2)*d)));
        halfSunHori.setBackgroundColor(Color.TRANSPARENT);



        screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();
        sunLeft=screenWidth/4;
        sunTop=screenHeight/4;


        if(CHK_IF_RESUME==0)
        {
            leftmargin[0] = sunLeft;
            topMargin[0] = sunTop;
            pos++;
        }

        sun=new ImageButton(this);
        sun.setImageResource(R.drawable.sun);
        sun.setVisibility(View.VISIBLE);
        sun.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        sun.setX(sunLeft);
        sun.setY(sunTop);
        sun.setLayoutParams(new RelativeLayout.LayoutParams((int)(sunWidth*d),(int)(sunHeight*d)));
        sun.setBackgroundColor(Color.TRANSPARENT);


        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PlanetDetails.class);
                startActivity(intent);
                onPause();
            }
        });


        sun.setOnTouchListener(this);


        sunCenterX=sun.getX()+sun.getWidth()/2;
        sunCenterY=sun.getY()+sun.getHeight()/2;

        for(int a=0;a<NO_OF_PLANETS;a++)
        {
            float tempX=0,tempY=0,chk=0;
            int angle=0;
            if(CHK_IF_RESUME==0)
            {
                while (chk == 0) {
                    chk = 1;
                    //Min + (int)(Math.random() * ((Max - Min) + 1))
                    int orbitNo=1+(int)(Math.random()*((quantizedOrbits.length-1)+1));
                    orbitNo--;
                    angle=0+(int) (Math.random()*(360-0)+1);
                    tempX =(float)((quantizedOrbits[orbitNo])*Math.cos(angle)+sunCenterX);
                    tempY = (float)((quantizedOrbits[orbitNo])*Math.sin(angle)+sunCenterY);

                    for (int b = 0; b < pos; b++)
                    {
                        if (Math.abs(leftmargin[b] - tempX) <= 300 && Math.abs(topMargin[b] - tempY) <= 300)
                        {
                            chk = 0;
                            break;
                        }

                    }
                }

                leftmargin[pos] = tempX;
                topMargin[pos++] = tempY;
            }
            else
            {
                tempX=leftmargin[a+1];
                tempY=topMargin[a+1];
            }

            planets[a]=new ImageButton(this);
            planets[a].setImageResource(pics[a%pics.length]);
            planets[a].setVisibility(View.VISIBLE);
            planets[a].setScaleType(ImageButton.ScaleType.FIT_CENTER);
            planets[a].setBackgroundColor(Color.TRANSPARENT);
            planets[a].setLayoutParams(new RelativeLayout.LayoutParams((int)(75*d),(int)(75*d)));
            planets[a].setX(tempX);
            planets[a].setY(tempY);



            planets[a].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,PlanetDetails.class);
                    startActivity(intent);
                    onPause();
                }
            });

            planets[a].setOnTouchListener(this);


        }
        sunCenterX=sun.getX()+sun.getWidth()+225;
        sunCenterY=sun.getY()+sun.getHeight()+225;

        layout.setOnTouchListener(this);
        for (int a = 0; a<NO_OF_PLANETS ;a++)
        {
            planets[a].setVisibility(View.GONE);
            layout.addView(planets[a]);
            if(a==NO_OF_PLANETS-1)
            {
                layout.addView(sun);
                layout.addView(halfSun);
                layout.addView(halfSunHori);
            }
        }
        int temp=0;
        for (int a = 0; a<quantizedOrbits.length ;a++)      //One-By-One orbit
        {
            if(temp<=6)
                temp++;
            final int index=a;
            handler1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    orbitRegion.draw(sunCenterX, sunCenterY, quantizedOrbits, d, index,0);
                }
            }, 100 * temp);
        }
        for(int a=0;a<=10;a++)       //Orbit-animation
        {
            temp++;
            final int val=a;
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orbitRegion.draw(sunCenterX, sunCenterY, quantizedOrbits, d, quantizedOrbits.length,val);
                }
            },100*temp);
        }
        for(int a=9;a>=-10;a--)       //Orbit-animation
        {
            temp++;
            final int val=a;
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orbitRegion.draw(sunCenterX, sunCenterY, quantizedOrbits, d, quantizedOrbits.length,val);
                }
            },100*temp);
        }
        for(int a=-9;a<=0;a++)       //Orbit-animation
        {
            temp++;
            final int val=a;
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orbitRegion.draw(sunCenterX, sunCenterY, quantizedOrbits, d, quantizedOrbits.length,val);
                }
            },100*temp);
        }
        for (int a = 0; a<NO_OF_PLANETS ;a++)       //One-by-One planets
        {
            temp++;
            final int index=a;
            handler2.postDelayed(new Runnable() {

                @Override
                public void run() {
                    planets[index].setVisibility(View.VISIBLE);
                }
            }, 100 * temp);
        }

    }
    public static void incr()
    {
        IDX++;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        CHK_IF_RESUME=1;
        stars.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CHK_IF_RESUME=1;
//        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.flying_stars);
//        stars.setVideoURI(uri);
//        stars.start();
//        stars.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                stars.start();
//            }
//        });
    }



    @Override
    public boolean onTouch(View v, MotionEvent motionEvent)
    {

        final int action= motionEvent.getAction();
        switch(action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                startTime= Calendar.getInstance().getTimeInMillis();
                setStartX((int)motionEvent.getRawX());
                setStartY((int)motionEvent.getRawY());

                //Toast.makeText(this,"down",Toast.LENGTH_SHORT).show();

                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                setCurrentX((int)motionEvent.getRawX());
                setCurrentY((int)motionEvent.getRawY());

                //Toast.makeText(this,"move",Toast.LENGTH_SHORT).show();

                float diffX=startX-currentX;
                float diffY=startY-currentY;

                setStartX(currentX);
                setStartY(currentY);

                sun.setX((sun.getX()-diffX));
                sun.setY((sun.getY()-diffY));

                placeHalfSun();

                for(int a=0;a<NO_OF_PLANETS;a++)
                {
                    planets[a].setX((planets[a].getX()-diffX));
                    planets[a].setY((planets[a].getY()-diffY));
                }
                sunCenterX=sun.getX()+sun.getWidth()/2;
                sunCenterY=sun.getY()+sun.getHeight()/2;
                orbitRegion.draw(sunCenterX, sunCenterY, quantizedOrbits, d, quantizedOrbits.length,0);

                break;
            }
            case MotionEvent.ACTION_UP:
            {
                endTime=Calendar.getInstance().getTimeInMillis();
                if(endTime-startTime<=200 && !v.equals(layout))
                    v.performClick();
                orbitRegion.draw(sunCenterX, sunCenterY, quantizedOrbits, d, quantizedOrbits.length,0);
                break;
            }

        }
        return true;
    }

    static void setCurrentX(int x)
    {
        currentX=x;
    }
    static void setCurrentY(int y)
    {
        currentY=y;
    }
    static void setStartX(int x)
    {
        startX=x;
    }
    static void setStartY(int y)
    {
        startY=y;
    }

    public void placeHalfSun()
    {
        float sunX,sunY;
        sunX=sun.getX();
        sunY=sun.getY();

        if(sunY<0-(sunHeight*d)/2 && sunX>=0-(sunWidth*d)/2)       //Upper part
        {
            sun.setVisibility(View.GONE);
            halfSunHori.setVisibility(View.VISIBLE);
            halfSun.setVisibility(View.GONE);
            halfSunHori.setImageResource(R.drawable.bottom_sun);
            halfSunHori.setY(-50);
            if(sunX>screenWidth-300)
            {
                halfSunHori.setX(screenWidth-300);
            }
            else
            {
                halfSunHori.setX(sunX);
            }
        }
        else if(sunX>screenWidth-(sunWidth*d)/2 && sunY>=0-(sunHeight*d)/2)       //Right part
        {
            sun.setVisibility(View.GONE);
            halfSun.setVisibility(View.VISIBLE);
            halfSunHori.setVisibility(View.GONE);
            halfSun.setImageResource(R.drawable.left_sun);
            halfSun.setX(screenWidth-sunWidth);
            if(sunY>screenHeight-600)
            {
                halfSun.setY(screenHeight-600);
            }
            else
            {
                halfSun.setY(sunY);
            }
        }
        else if(sunY>screenHeight-(sunHeight*d) && sunX<=screenWidth)       //Lower part
        {
            sun.setVisibility(View.GONE);
            halfSunHori.setVisibility(View.VISIBLE);
            halfSun.setVisibility(View.GONE);
            halfSunHori.setImageResource(R.drawable.top_sun);
            halfSunHori.setY(screenHeight-(sunWidth*d)/1.25f);
            if(sunX<0-300)
            {
                halfSunHori.setX(0-300);
            }
            else
            {
                halfSunHori.setX(sunX);
            }
        }
        else if(sunX<0-(sunWidth*d)/2 && sunY<=screenHeight)       //Left part
        {
            sun.setVisibility(View.GONE);
            halfSun.setVisibility(View.VISIBLE);
            halfSunHori.setVisibility(View.GONE);
            halfSun.setImageResource(R.drawable.right_sun);
            halfSun.setX(0);
            if(sunY<0-300)
            {
                halfSun.setY(0-300);
            }
            else
            {
                halfSun.setY(sunY);
            }
        }
        else
        {
            sun.setVisibility(View.VISIBLE);
            halfSun.setVisibility(View.GONE);
            halfSunHori.setVisibility(View.GONE);
        }
    }
    public static double distance(float p1x,float p1y, float p2x, float p2y)
    {
        return(Math.sqrt(Math.pow(p1x-p2x,2)+Math.pow(p1y-p2y,2)));
    }
}

