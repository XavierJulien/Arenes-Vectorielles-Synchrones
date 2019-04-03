public class RemoteCalculette implements IRemoteCalculette
{
 int cumul = 0;
 public RemoteCalculette() { super(); }
 public Integer eval(Integer op1, Integer op2) { return new Integer(op1.intValue() + op1.intValue()) ; }
 public void cumuler(Integer val) { cumul += val.intValue(); }
 public Integer getCumul() { return new Integer(cumul); }
 public void resetCumul() { cumul = 0; }
}