import java.lang.reflect.InvocationHandler; 
import java.lang.reflect.Method; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream;

public class RMIHandler implements InvocationHandler 
{     
 private ObjectInputStream ois;     
 private ObjectOutputStream oos;     
 private String name;      
	
 public RMIHandler(String name, ObjectInputStream ois, ObjectOutputStream oos) 
 {         
  this.name = name;         
  this.ois = ois;         
  this.oos = oos;     
 }  
	
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
 {         
  oos.writeObject(new InvocationContext(name, args, method.getName()));
  return ois.readObject();     
 } 
}