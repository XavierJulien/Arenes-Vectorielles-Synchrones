import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Drawer {

	private GraphicsContext ctx;
	private SpaceRun s;
	private double ve_radius = SpaceRun.ve_radius;
	private double ob_radius = SpaceRun.ve_radius;
	
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
			posx = ship.get_posX();
			posy = ship.get_posY();
			angle = Math.toRadians(ship.getAngle());
			ve_radius = ship.getRadius();
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
				ctx.setFill(Color.DARKRED);
			}else {
				ctx.setFill(Color.YELLOW);
			}
			ctx.fillOval(posx-ship.getRadius(),posy-ship.getRadius(), ship.getRadius()*2, ship.getRadius()*2);
			ctx.setFill(Color.RED);
			ctx.fillPolygon(xval, yval, 4);
			
		}
	}
	
	public void drawTarget() {
		//System.out.println("x : "+s.getTarget().getX()+",y : "+s.getTarget().getY());
		ctx.setFill(Color.GOLDENROD);
		ctx.fillOval(s.getTarget().getX(), s.getTarget().getY(), ob_radius, ob_radius);
	}
}
