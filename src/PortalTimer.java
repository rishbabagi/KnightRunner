import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PortalTimer 
{
    public boolean active = false;
    public int timeLeft = 5;
    private Timer timer;

    public void start() 
    {
        active = true;
        timeLeft = 5;
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() 
            {
                timeLeft--;
                if (timeLeft <= 0) 
                {
                    active = false;
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }

    public void stop() 
    {
        active = false;
        if (timer != null) timer.cancel();
    }

    public void draw(Graphics g, int x, int y, Font font) 
    {
        if (active) 
        {
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(String.valueOf(timeLeft), x, y);
        }
    }
}
