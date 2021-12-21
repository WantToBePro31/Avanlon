package entity;

import ui.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity
{
    private final int screenX;
    private final int screenY;

    public Player(GamePanel gp)
    {
        super(gp);

        imgHeight = 64;
        imgWidth = 64;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(10, 32, 32, 32);
        setSolidAreaDefaultX(solidArea.x);
        setSolidAreaDefaultY(solidArea.y);

        setDefaultValues();
        getPlayerImage();
    }

    private void setDefaultValues()
    {
        setXAndY(gp.getMapPick());
        setSpeed(4);
        setDirection("down");
        spriteNum = 2;
        spriteCounter = 0;
        dialogueIndex = 0;
    }

    public void setXAndY(int i)
    {
        switch (i)
        {
            case 0 -> {
                setWorldX((int)(gp.tileSize * 11.5));
                setWorldY(gp.tileSize * 34);
            }
            case 1 -> {
                setWorldX(gp.tileSize * 23);
                setWorldY(gp.tileSize * 21);
            }
            case 2 -> {
                setWorldX(gp.tileSize * 16);
                setWorldY(gp.tileSize * 32);
            }
            case 3 -> {
                setWorldX(gp.tileSize * 9);
                setWorldY(gp.tileSize * 34);
            }
            case 4 -> {
                setWorldX(gp.tileSize * 20);
                setWorldY(gp.tileSize * 21);
            }
        }
    }

    private void getPlayerImage()
    {
        String txt = "/character/32x48/archer/archer";
        down1 = setup(txt + " 1", 1.5, true);
        down2 = setup(txt + " 2", 1.5, true);
        down3 = setup(txt + " 3", 1.5, true);
        left1 = setup(txt + " 4", 1.5, true);
        left2 = setup(txt + " 5", 1.5, true);
        left3 = setup(txt + " 6", 1.5, true);
        right1 = setup(txt + " 7", 1.5, true);
        right2 = setup(txt + " 8", 1.5, true);
        right3 = setup(txt + " 9", 1.5, true);
        up1 = setup(txt + " 10", 1.5, true);
        up2 = setup(txt + " 11", 1.5, true);
        up3 = setup(txt + " 12", 1.5, true);
    }

    @Override
    public void update()
    {
        if(gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed)
        {
            if (gp.keyH.upPressed)
                setDirection("up");
            else if (gp.keyH.downPressed)
                setDirection("down");
            else if (gp.keyH.leftPressed)
                setDirection("left");
            else setDirection("right");

            // Check Tile Collision
            this.setCollisionOn(false);
            gp.cChecker.checkTile(this);

            if(!isCollisionOn())
            {
                switch (getDirection())
                {
                    case "up" -> setWorldY(getWorldY() - getSpeed());
                    case "down" -> setWorldY(getWorldY() + getSpeed());
                    case "left" -> setWorldX(getWorldX() - getSpeed());
                    case "right" -> setWorldX(getWorldX() + getSpeed());
                }
            }

            spriteCounter++;
            if (spriteCounter > 8)
            {
                if (spriteNum == 1)
                    spriteNum = 3;
                else if (spriteNum == 3  || spriteNum == 2)
                    spriteNum = 1;
                spriteCounter = 0;
            }
        }
        else
        {
            standCounter++;
            if(standCounter == 8)
            {
                spriteNum = 2;
                standCounter = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2)
    {
        BufferedImage image = null;

        switch (getDirection())
        {
            case "up" -> {
                if (spriteNum == 1)
                    image = up1;
                else if (spriteNum == 2)
                    image = up2;
                else if (spriteNum == 3)
                    image = up3;
            }
            case "down" -> {
                if (spriteNum == 1)
                    image = down1;
                else if (spriteNum == 2)
                    image = down2;
                else if (spriteNum == 3)
                    image = down3;
            }
            case "left" -> {
                if (spriteNum == 1)
                    image = left1;
                else if (spriteNum == 2)
                    image = left2;
                else if (spriteNum == 3)
                    image = left3;
            }
            case "right" -> {
                if (spriteNum == 1)
                    image = right1;
                else if (spriteNum == 2)
                    image = right2;
                else if (spriteNum == 3)
                    image = right3;
            }
        }

        // Stop moving camera at the edge
        int x = screenX;
        int y = screenY;

        if(screenX > worldX)
            x = worldX;
        if(screenY > worldY)
            y = worldY;

        int rightOffset = gp.screenWidth - screenX;
        if(rightOffset > gp.getWorldWidth() - worldX)
            x = gp.screenWidth - (gp.getWorldWidth() - worldX);

        int bottomOffset = gp.screenHeight - screenY;
        if(bottomOffset > gp.getWorldHeight() - worldY)
            y = gp.screenHeight - (gp.getWorldHeight() - worldY);

        g2.drawImage(image, x, y, null);
        g2.setColor(Color.red);
        g2.drawRect(solidArea.x + screenX, solidArea.y + screenY, solidArea.width, solidArea.height);
    }

    // Getter
    public int getScreenX() { return screenX; }
    public int getScreenY() { return screenY; }
}
