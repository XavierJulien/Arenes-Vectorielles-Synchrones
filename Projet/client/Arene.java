public class Arene{
  @SuppressWarnings("unused")
private float cx,cy;//coordonnées centre
  private float demih,demil;//demihauteur/demilargeur

  public Arene(float demih,float demil){
    this.cx = 0;
    this.cy = 0;
    this.demih = demih;
    this.demil = demil;
  }

  public void move(Point p,float addx,float addy){
    if(p.getX()+addx > demil){
      if(p.getY()+addy > demih){
        p.setX(p.getX()-(2*demil)+addx);
        p.setY(p.getY()-(2*demih)+addy);
      }else{
        p.setX(p.getX()-(2*demil)+addx);
        p.setY(p.getY()+addy);
      }
    }else{
      if(p.getY()+addy > demih){
        p.setX(p.getX()+addx);
        p.setY(p.getY()-(2*demih)+addy);
      }else{
        p.setX(p.getX()+addx);
        p.setY(p.getY()+addy);
      }
    }
  }

  public static void main(String[] args) {
    Arene a = new Arene((float)5.0,(float)5.0);
    Point p = new Point((float)0.0,(float)0.0);
    System.out.println("{x = "+p.getX()+";y="+p.getY()+"}");
    a.move(p,(float)2.0,(float)0.0);
    System.out.println("{x = "+p.getX()+";y="+p.getY()+"}");
    a.move(p,(float)0.0,(float)6.0);
    System.out.println("{x = "+p.getX()+";y="+p.getY()+"}");
    a.move(p,(float)2.0,(float)0.0);
    System.out.println("{x = "+p.getX()+";y="+p.getY()+"}");
    a.move(p,(float)2.0,(float)0.0);
    System.out.println("{x = "+p.getX()+";y="+p.getY()+"}");
  }
}
