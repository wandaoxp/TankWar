package cn.wn.tankwar.missile;

import cn.wn.tankwar.TankClient;
import cn.wn.tankwar.explode.Explode;
import cn.wn.tankwar.explode.ExplodeView;
import cn.wn.tankwar.interfaces.Controller;
import cn.wn.tankwar.tank.Tank;

/**
 * �ӵ�������
 * 
 * @author Wangning
 * 
 */
public class MissileController implements Controller {

	private static final int SPEED = 6;
	private Missile missile;
	private TankClient tc;

	public MissileController(TankClient tc) {
		this.tc = tc;
	}

	public void attach(Missile missile) {
		this.missile = missile;
	}

	@Override
	public void move() {
		if (!missile.isAlive()) {
			return;
		}
		if (isHit()) {
			Explode explode = new Explode(0, 0, 56, 56, new ExplodeView());
			explode.setCenter(missile.getCenterPoint().x,
					missile.getCenterPoint().y);
			tc.explodes.add(explode);
			missile.setAlive(false);
			return;
		}
		switch (missile.getDirection()) {
		case U:
			moveUp();
			break;
		case D:
			moveDown();
			break;
		case L:
			moveLeft();
			break;
		case R:
			moveRight();
			break;
		case LU:
			moveUp();
			moveLeft();
			break;
		case RU:
			moveUp();
			moveRight();
			break;
		case LD:
			moveDown();
			moveLeft();
			break;
		case RD:
			moveDown();
			moveRight();
			break;

		default:
			break;
		}
	}

	private boolean isHit() {
		boolean hit = false;
		if (missile.getRect().intersects(tc.getObtacle().getRect())) {
			hit = true;
		}else if (missile.getX()<0||missile.getX()>tc.getWidth()-missile.getWidth()) {
			hit = true;
		}else if (missile.getY()<0||missile.getY()>tc.getHeight()-missile.getHeight()) {
			hit = true;
		} else {
			for (Tank tank : tc.getTanks()) {
				if (missile.getRect().intersects(tank.getRect())
						&& tank.isGood() != missile.isGood()) {
					hit = true;
					tank.getController().beingHit();
				}
			}
		}
		return hit;
	}

	private void moveRight() {
		missile.setX(missile.getX() + SPEED);
	}

	private void moveLeft() {
		missile.setX(missile.getX() - SPEED);
	}

	private void moveDown() {
		missile.setY(missile.getY() + SPEED);
	}

	private void moveUp() {
		missile.setY(missile.getY() - SPEED);
	}

}
