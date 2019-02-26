import java.util.Map; 
import java.util.HashMap; 
import java.util.List; 
import java.util.ArrayList; 
import java.net.ServerSocket; 
import java.net.Socket; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.lang.reflect.Proxy ; 

public class Registry 
{     
    private Map <String, Object> services = new HashMap<String, Object>();     
    private Socket server;     
    private ObjectInputStream ois;     
    private ObjectOutputStream oos;     
    private ServerSocket ss;      
	
    public Registry register(String name, Object service) 
    {         
	services.put(name, service);         
	return this;     
    }      
	
    public void publish(int port) 
    { 
	if (server != null) 
	    {             
		throw new InvalidStateException(
			"Socket is already connected to a    server");         
	    }                  
	try 
	    {             
		ss = new ServerSocket(port);             
		while(true) 
		    {                 
			final Socket s = ss.accept();
                
			new Thread () 
			{                     
			    public void run () 
			    { 
				try {                             
				    ObjectOutputStream oos =
					new ObjectOutputStream(
					       s.getOutputStream());
				    ObjectInputStream ois =
					new ObjectInputStream(
					       s.getInputStream());
				    InvocationContext ic =
					(InvocationContext) ois.readObject();
				    Object targetObject =
					services.get(ic.getName());
				    Object result =
					targetObject.getClass().
					getMethod(ic.getMethod(),
						  args2Class(ic.getArgs())).
					invoke(targetObject, ic.getArgs());
				    oos.writeObject(result);
				} catch (Exception e) 
				    { throw new RMIInvocationException(
						      e.getMessage(), e);
				    } 
			    }                 
			}.start();             
		    }         
	    } catch (Exception e) 
	    { throw new RMIInvocationException(e.getMessage(), e); }      
    }      
	
    public Registry connect(String host, int port) 
    {         
	if (server != null) 
	    { throw new InvalidStateException("Socket is already connected");}
	if (ss != null) 
	    { throw new InvalidStateException("Registry is listening"); }
	try 
	    {             
		server = new Socket(host, port);             
		ois = new ObjectInputStream(server.getInputStream());
		oos = new ObjectOutputStream(server.getOutputStream());
	    } catch (Exception e) 
	    { throw new RMIInvocationException(e.getMessage(), e); }            
	return this;     
    }      
	
    public <T> T get(String name, Class<T> clazz)
    {         
	return (T) Proxy.newProxyInstance(
		     ClassLoader.getSystemClassLoader(),
		     new Class[]{clazz}, new RMIHandler(name, ois, oos));    
    }      
	
    private Class[] args2Class (Object[] objs) 
    {
	List<Class> classes = new ArrayList<Class>();         
	for (Object o : objs) 
	    { classes.add(o.getClass());}         
	return classes.toArray(new Class[]{});     
    } 
} 
