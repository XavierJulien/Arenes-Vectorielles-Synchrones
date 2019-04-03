public interface IRemoteCalculette
{
 public Integer eval(Integer op1, Integer op2);
 public void cumuler(Integer val);
 public Integer getCumul();
 public void resetCumul();
}