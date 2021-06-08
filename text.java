package l1;

import java.awt.*;  
import java.awt.event.*;  
public class text extends Frame implements ActionListener{  
    TextField tf; Label l; Button b;  
    text(){  
        tf=new TextField();  
        tf.setBounds(50,50, 150,20);  
        l=new Label("Enter Uid");  
        l.setBounds(50,100, 250,20);      
        b=new Button("Submit");  
        b.setBounds(50,150,60,30);  
        b.addActionListener(this);    
        add(b);add(tf);add(l);    
        setSize(1000,1000);  
        setLayout(null);  
        setVisible(true); 
       
        
    }  
    public void actionPerformed(ActionEvent e) {  
        try{  
        		App o = new App();
			String a = tf.getText();
			if(a == null) {
				String details100 = o.findRank();
				System.out.println(details100);
				l.setText(details100); 
			}
			else {
				int rank = o.findRank(a);
		        l.setText("Rank of the User is :" +String.valueOf(rank)); 

			}

			//l.setText("Enter Uid");

			// set the text of field to blank
        
        }catch(Exception ex){System.out.println(ex);}  
    }  
  
}  