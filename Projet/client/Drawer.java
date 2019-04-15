
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Drawer {

	//Context
	private GraphicsContext ctx;
	private DataBase database;
	
	//Constructor
	public Drawer(DataBase database,GraphicsContext ctx) {
		this.database = database;
		this.ctx = ctx;
	}

	//Draw elements
	public void drawBackground() {
		ctx.clearRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
		ctx.drawImage(Constantes.space, 0, 0, ctx.getCanvas().getWidth(),ctx.getCanvas().getHeight());
	}
	public void drawWaiting() {
		ctx.setStroke(Paint.valueOf("white"));
		ctx.setFont(new javafx.scene.text.Font("Verdana", 50));
		ctx.strokeText("Waiting for the session ... ", 150, 350);
	}
	public void drawPlayers() {
		double posx,posy,angle;
		double x1,y1;
		double x2,y2;
		double x3,y3;
		double x4,y4;
		for(Player p : database.getPlayer_list().values()) {
			Ship ship = p.getShip();
			posx = ship.get_posX()+Constantes.demil;
			posy = Constantes.demih-ship.get_posY();
			angle = Math.toRadians(ship.getAngle());
			x1 = posx + Constantes.ve_radius * Math.cos(angle);
			y1 = posy - Constantes.ve_radius * Math.sin(angle);
			x2 = posx + Constantes.ve_radius * Math.cos(angle + Math.PI + Math.PI/5);
			y2 = posy - Constantes.ve_radius * Math.sin(angle + Math.PI + Math.PI/5);
			x3 = posx + Constantes.ve_radius/2 * Math.cos(angle + Math.PI);
			y3 = posy - Constantes.ve_radius/2 * Math.sin(angle + Math.PI);
			x4 = posx + Constantes.ve_radius * Math.cos(angle - Math.PI - Math.PI/5);
			y4 = posy - Constantes.ve_radius * Math.sin(angle - Math.PI - Math.PI/5);
			double[] xval = {x1,x2,x3,x4};
			double[] yval = {y1,y2,y3,y4};
			if(p == database.getMyself()) {
				ctx.setFill(Color.INDIANRED);
				ctx.fillOval(posx-Constantes.ve_radius,posy-Constantes.ve_radius, Constantes.ve_radius*2, Constantes.ve_radius*2);
				ctx.setFill(Color.DARKRED);
				ctx.fillPolygon(xval, yval, 4);
			}else {
				ctx.setFill(Color.CORNFLOWERBLUE);
				ctx.fillOval(posx-Constantes.ve_radius,posy-Constantes.ve_radius, Constantes.ve_radius*2, Constantes.ve_radius*2);
				ctx.setFill(Color.ROYALBLUE);
				ctx.fillPolygon(xval, yval, 4);
			}
		}
	}
	public void drawTarget() {
		ctx.setFill(Color.GOLDENROD);
		double x = database.getTarget().getX()+Constantes.demil;
		double y = Constantes.demih-database.getTarget().getY();
		ctx.fillOval(x-Constantes.ta_radius,y-Constantes.ta_radius, Constantes.ta_radius*2, Constantes.ta_radius*2);
		ctx.drawImage(Constantes.coin, x-Constantes.ta_radius,y-Constantes.ta_radius, Constantes.ta_radius*2, Constantes.ta_radius*2);
	}
	public void drawObstacles() {
		for (Point p : database.getObstacles_list()) {
			double x = p.getX()+Constantes.demil;
			double y = Constantes.demih-p.getY();
			ctx.drawImage(Constantes.asteroid,x-Constantes.ob_radius,y-Constantes.ob_radius, Constantes.ob_radius*2, Constantes.ob_radius*2);
		}
	}
	public void drawPieges() {
		for (Point p : database.getPieges_list()) {
			double x = p.getX()+Constantes.demil;
			double y = Constantes.demih-p.getY();
			ctx.drawImage(Constantes.piege,x-Constantes.pi_radius*2,y-Constantes.pi_radius*2, Constantes.pi_radius*4, Constantes.pi_radius*4);
		}
	}
	public void drawLaser() {
		ArrayList<Ship> list = database.getLasers_list();
		for (Ship p : list) {
			double x = p.get_posX()+Constantes.demil;
			double y = Constantes.demih-p.get_posY();
			ctx.drawImage(Constantes.laser,x-Constantes.la_radius,y-Constantes.la_radius, Constantes.la_radius*2, Constantes.la_radius*2);
		}
	}
}
