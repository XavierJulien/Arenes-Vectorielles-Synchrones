public class PlayerScore{
  private String user;
  private String score;

  public PlayerScore(String user,String score){
    this.user = user;
    this.score = score;
  }

  public String getUser(){return user;}
  public String getScore(){return score;}
}
