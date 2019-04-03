public class InvocationContext implements Serializable 
{     
 private Object[] args;     
 private String method;     
 private String name;     
	
 public InvocationContext() {     } 
	
 public InvocationContext(String name, Object[] args, String method) 
 {         
  this.name = name;         
  this.args = args;         
  this.method = method;     
 }      
	
 // + fonctions get et set pour tous les arguments
	
} 