package packageClient;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Drawer {

	private GraphicsContext ctx;
	private SpaceRun s;
	private double ve_radius = SpaceRun.ve_radius;
	private double ob_radius = SpaceRun.ob_radius;
	private double pi_radius = SpaceRun.pi_radius;
	private double objectif_radius = 20.0;
	private Image coin = new Image("images/star.png");
	private Image asteroid = new Image("images/asteroidbis.png");
	private Image piege = new Image("images/banana.png");

	public Drawer(SpaceRun s) {
		this.s = s;
		ctx = s.getGraphicsContext();

	}

	public void drawPlayers() {
		double posx,posy,angle;
		double x1,y1;
		double x2,y2;
		double x3,y3;
		double x4,y4;
		for(Player p : s.getPlayer_list().values()) {
			Ship ship = p.getShip();
			posx = ship.get_posX()+s.getDemil();
			posy = s.getDemih()-ship.get_posY();
			angle = Math.toRadians(ship.getAngle());
			x1 = posx + ve_radius * Math.cos(angle);
			y1 = posy - ve_radius * Math.sin(angle);
			x2 = posx + ve_radius * Math.cos(angle + Math.PI + Math.PI/5);
			y2 = posy - ve_radius * Math.sin(angle + Math.PI + Math.PI/5);
			x3 = posx + ve_radius/2 * Math.cos(angle + Math.PI);
			y3 = posy - ve_radius/2 * Math.sin(angle + Math.PI);
			x4 = posx + ve_radius * Math.cos(angle - Math.PI - Math.PI/5);
			y4 = posy - ve_radius * Math.sin(angle - Math.PI - Math.PI/5);
			double[] xval = {x1,x2,x3,x4};
			double[] yval = {y1,y2,y3,y4};
			if(p == s.getMyself()) {
				ctx.setFill(Color.INDIANRED);
				ctx.fillOval(posx-ve_radius,posy-ve_radius, ve_radius*2, ve_radius*2);
				ctx.setFill(Color.DARKRED);
				ctx.fillPolygon(xval, yval, 4);
			}else {
				ctx.setFill(Color.CORNFLOWERBLUE);
				ctx.fillOval(posx-ve_radius,posy-ve_radius, ve_radius*2, ve_radius*2);
				ctx.setFill(Color.ROYALBLUE);
				ctx.fillPolygon(xval, yval, 4);
			}
		}
	}

	public void drawTarget() {
		ctx.setFill(Color.GOLDENROD);
		double x = s.getTarget().getX()+s.getDemil();
		double y = s.getDemih()-s.getTarget().getY();
		ctx.fillOval(x-objectif_radius,y-objectif_radius, objectif_radius*2, objectif_radius*2);
		ctx.drawImage(coin, x-objectif_radius,y-objectif_radius, objectif_radius*2, objectif_radius*2);
	}

	public void drawObstacles() {
		for (Point p : s.getObstacles_list()) {
			double x = p.getX()+s.getDemil();
			double y = s.getDemih()-p.getY();
			ctx.drawImage(asteroid,x-ob_radius,y-ob_radius, ob_radius*2, ob_radius*2);
		}
	}

	public void drawPieges() {
		for (Point p : s.getPieges_list()) {
			double x = p.getX()+s.getDemil();
			double y = s.getDemih()-p.getY();
			ctx.drawImage(piege,x-pi_radius*2,y-pi_radius*2, pi_radius*4, pi_radius*4);
		}
	}
}
