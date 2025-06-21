package entities;

import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;



// to change animation, just need to change the value in Constants class, like JUMP, FALLING,....
public class Player extends Entity{
	
	// Follow youtube
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 40;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down, jump;
	// Change speed here
	private float playerSpeed = 1.5f;
	private int[][] lvlData;
	
	// Change size hitbox here (maybe, not pretty sure:), default is xoffset = 21, yoffset = 4)
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;
	
	// Jumping / Gravity
	private float airSpeed = 0;
	private float gravity = 0.04f * Game.SCALE;
	// Change Jump height here
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	
	// Executive method/ constructor
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		
		// To change if we want the feet exceed the ground, change the height here( the last number * Game.SCALE)
		initHitbox(x, y, 20*Game.SCALE, 27*Game.SCALE);
		
	}
	
	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g) {
		// Change the size of the characters here
		g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
//		drawHitbox(g);
	}

	private void updateAnimationTick() {
		
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
			}
		}
		
	}
	
	private void setAnimation() {
		int startAni = playerAction;
		
		if (moving) {
			playerAction = RUNNING;
		} else {
			playerAction = IDLE;
		}
		
		if (inAir) {
			if (airSpeed < 0) 
				playerAction = JUMP;
			else 
				playerAction = FALLING;
		}
		
		if (attacking) {
			playerAction = ATTACK_1;
		}
		
		if (startAni != playerAction) {
			resetAniTick();
		}
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		
		if(jump)
			jump();
		if (!left && !right && !inAir)
			return;
		
		float xSpeed = 0;
		
		if (left) 
			xSpeed -= playerSpeed;
		if (right)
			xSpeed += playerSpeed;
		
		if (!inAir) 
			if (!isEntityOnFloor(hitbox, lvlData)) 
				inAir = true;
		

		if(inAir) {
			if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		} else
			updateXPos(xSpeed);

		moving = true;
		
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
		
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
		
	}

	private void updateXPos(float xSpeed) {
		if(canMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
		
	}

	private void loadAnimations() {
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			
			animations = new BufferedImage[9][6];
			for (int j = 0; j < animations.length; j++) {
				for (int i = 0; i < animations[j].length; i++) {
					animations[j][i] = img.getSubimage(i*64, j*40, 64, 40);
				}
			}
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!isEntityOnFloor(hitbox, lvlData))
			inAir = true; 
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}

}
