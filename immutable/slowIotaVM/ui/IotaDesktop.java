/** Ben F Rayfield offers this software opensource MIT license */
package immutable.slowIotaVM.ui;
import static mutable.util.Lg.*;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import immutable.slowIotaVM.SlowVM;

public class IotaDesktop extends JPanel{

	public final IconsPanel iconsPanel;

	public IotaDesktop(Set<Icon> icons){
		super(new GridLayout(0,1));
		iconsPanel = new IconsPanel(icons,(JComponent j)->setDetails(j));
		add(iconsPanel,0);
		add(new JLabel("(empty)"),1);
		addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e){
				String s = "mouse moved: "+e;
				lg(s);
				setDetails(new JLabel(s));
			}
			public void mouseDragged(MouseEvent e){
				mouseMoved(e);
			}
		});
	}
	
	public void setDetails(JComponent details){
		remove(1);
		add(details);
		synchronized(getTreeLock()){
			validateTree();
		}
	}
	
	public static void main(String[] args){
		Set<Icon> icons = new HashSet();
		Icon iota = new Icon(SlowVM.iota, 50, 50, new int[1][1]);
		Icon iota2 = new Icon(SlowVM.iota, 50, 150, new int[1][1]);
		Icon reflect = new Icon(SlowVM.reflect, 200, 200, new int[1][1]);
		Icon reflect2 = new Icon(SlowVM.reflect, 200, 300, new int[1][1]);
		icons.add(iota); //can build anything out of 2 of each of the leafs
		icons.add(iota2); //can build anything out of 2 of each of the leafs
		icons.add(reflect);
		icons.add(reflect2);
		IotaDesktop d = new IotaDesktop(icons);
		JFrame window = new JFrame("IotaDesktop");
		window.setSize(500, 500);
		window.setLocation(200, 200);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(d);
		window.setVisible(true);
	}

}