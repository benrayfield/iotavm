/** Ben F Rayfield offers this software opensource MIT license */
package immutable.slowIotaVM.ui;
import static mutable.util.Lg.*;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import immutable.slowIotaVM.SlowVM;

/** iotadesktop is to programming what mindmap is to text. It will become an
extreme improvement to how people build and explore pure functional software,
using funcalls to measure and create funcalls. Remember to upgrade the UI as
needed, and for each funcall's Icon to have a java function that returns
JComponent detail view, and can grab functions out of such detail view,
and can right click an icon to delete it, and can fulleconacyc slowly to
help decide which things cost too much memory to keep, and can use the funcs
to help me test and redesign the system. This is a next level toward
techSingularity, that funcs start being tools to understand and build funcs,
with Human help, but AIs can do it too later, as some of the funcs will
be (opencl optimized) AIs understanding other funcs. Think of it like a
puzzle game where the puzzle is how to build AIs and games and more
interesting puzzles. I've been doing this for years in other programming
languages that werent pure functional, cuz so few of them (if any) truly
are, and its frustrating, but functions are about to help me and soon after
that they will come to life as very basic selfModifying (in forkEdit ways) AIs.


iotavmIconPerFuncDragOntoAnotherIconToCallAndGetAnotherIconYoureDragging

Do this NEXT, before fixing any more of the design problems,
cuz slowvm is working in very basic way. Save the funcs and icon positions
and prehopfieldnames and icons (or generators of the icons?),
cuz I need it to help me test them,
and will be good to experience it visually.
<br><br>
These can do anything, such as acyclicFlow music funcs
(of constant memory size optimized), learnloop rbm, simple games,
learn math, organize immutable files, etc.
<br><br>
Each icon can have a prehopfieldName andOr icon drawn or generated.
<br><br>
There will be various views of the nodes (each node is a funcall),
and later some interactive views like hooking into jsoundcard and
graphics, but the basic view will be a toString of the code.
<br><br>
I want optional fullEconacyc that runs slow and displays on icons
(maybe as color andOr number) to help in keeping size down.
<br><br>
To start with, state will be a single json file, but later versions
will have more optimized storage approaching 100% binary efficiency
such as by tlapp.
*/
public class IconsPanel extends JPanel{
	
	//"TODO fix duplicate code between mousePressed-else and mouseReleased"
	
	//"TODO JComponent Icon.viewer() should by default be a noneditable textarea of the code. This will work until it gets too big, and then ill have to make more advanced viewers."
	
	
	/** mutable Set */
	public final Set<Icon> icons;
	
	//public int mouseY, mouseX;
	
	//public final boolean[] mouseButtons = new boolean[3];
	
	public Icon draggingOrNull;
	
	/** move prev icon to here when create a new one by funcall */
	public double dragStartedAtY, dragStartedAtX;
	
	public double touchDistance = 50;
	
	public final Consumer<JComponent> onSetDetails;
	
	/** mutable Set. onSetDetails displays JComponent as details of selected Icon in IotaDesktop */
	public IconsPanel(Set<Icon> icons, Consumer<JComponent> onSetDetails){
		this.icons = icons;
		this.onSetDetails = onSetDetails;
		addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					if(draggingOrNull == null){ //delete nearest
						Icon nearestOther = getNearestIconExceptTheOneDragging(e.getY(), e.getX());
						lg("mousePressed deleting nearest "+SlowVM.toString(nearestOther.ob));
						icons.remove(nearestOther);
						
						onSetDetails.accept(new JLabel("(TODO check if that was the one removed)"));
					}else{ //delete whats being dragged
						lg("mousePressed deleting drag "+SlowVM.toString(draggingOrNull.ob));
						icons.remove(draggingOrNull);
						draggingOrNull = null;
						onSetDetails.accept(new JLabel("(empty)"));
					}
				}else if(e.getButton() == MouseEvent.BUTTON1){
					if(draggingOrNull == null){ //start dragging
						draggingOrNull = getNearestIconExceptTheOneDragging(e.getY(), e.getX());
						dragStartedAtY = draggingOrNull.y;
						dragStartedAtX = draggingOrNull.x;
						lg("mousePressed Start-Drag "+SlowVM.toString(draggingOrNull.ob));
						onSetDetails.accept(draggingOrNull.viewer());
					}else{
						//TODO fix duplicate code between mousePressed-else and mouseReleased
						Icon nearestOther = getNearestIconExceptTheOneDragging(e.getY(), e.getX());
						double distance = draggingOrNull.distance(nearestOther);
						if(distance < touchDistance){ //dragging one Icon onto another
							Icon prevDragging = draggingOrNull;
							if(dragStartedAtY == dragStartedAtY){ //isnt 1./0
								lg("mouseReleased Setting y="+dragStartedAtY+" and x="+dragStartedAtX+" of "+SlowVM.toString(prevDragging.ob));
								prevDragging.y = dragStartedAtY;
								prevDragging.x = dragStartedAtX;
								dragStartedAtY = 1./0;
								dragStartedAtX = 1./0;
							}else{
								lg("Removing "+SlowVM.toString(prevDragging.ob));
								icons.remove(prevDragging);
							}
							draggingOrNull = draggingOrNull.onDragTo(nearestOther);
							lg("mouseReleased Creating "+SlowVM.toString(draggingOrNull.ob));
							icons.add(draggingOrNull);
							onSetDetails.accept(draggingOrNull.viewer());
						}else{
							lg("mousePressed Dropping "+SlowVM.toString(draggingOrNull.ob));
							draggingOrNull = null;
							//onSetDetails.accept(new JLabel("(empty)"));
						}
					}
				}
				repaint();
			}
			public void mouseReleased(MouseEvent e){
				//TODO fix duplicate code between mousePressed-else and mouseReleased
				if(draggingOrNull != null){
					Icon nearestOther = getNearestIconExceptTheOneDragging(e.getY(), e.getX());
					double distance = draggingOrNull.distance(nearestOther);
					if(distance < touchDistance){ //dragging one Icon onto another
						Icon prevDragging = draggingOrNull;
						if(dragStartedAtY == dragStartedAtY){ //isnt 1./0
							lg("mouseReleased Setting y="+dragStartedAtY+" and x="+dragStartedAtX+" of "+SlowVM.toString(prevDragging.ob));
							prevDragging.y = dragStartedAtY;
							prevDragging.x = dragStartedAtX;
							dragStartedAtY = 0./0;
							dragStartedAtX = 0./0;
						}else{
							lg("Removing "+SlowVM.toString(prevDragging.ob));
							icons.remove(prevDragging);
						}
						draggingOrNull = draggingOrNull.onDragTo(nearestOther);
						lg("mouseReleased Creating "+SlowVM.toString(draggingOrNull.ob));
						icons.add(draggingOrNull);
						onSetDetails.accept(draggingOrNull.viewer());
					}else{ //dropping the icon
						lg("mouseReleased Dropping "+SlowVM.toString(draggingOrNull.ob));
						draggingOrNull = null;
						//onSetDetails.accept(new JLabel("(empty)"));
					}
				}
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
		addMouseMotionListener(new MouseMotionListener(){
			public void mouseDragged(MouseEvent e){
				mouseMoved(e);
			}
			public void mouseMoved(MouseEvent e){
				if(draggingOrNull != null){
					draggingOrNull.y = e.getY();
					draggingOrNull.x = e.getX();
					repaint();
				}
			}
			
		});
	}
	
	public Icon getNearestIconExceptTheOneDragging(double y, double x){
		Icon i = null;
		double minDistance = Double.MAX_VALUE;
		for(Icon j : icons){
			if(j != draggingOrNull){
				double dy = y-j.y, dx = x-j.x;
				double distance = Math.sqrt(dy*dy+dx*dx);
				if(distance < minDistance){
					i = j;
					minDistance = distance;
				}
			}
		}
		return i;
	}
	
	public void paint(Graphics g){
		//TODO optimize by getting viewport to only draw the relevant area,
		//cuz this is likely to be very big in a much smaller scrollable area.
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for(Icon icon : icons){
			//TODO repeatable order so they dont flash which is in front of which others
			
			/*TODO paint the int[][] pics and String "name" keys.
			
			TODO have an area at bottom for a factory of JPanel to create
			view of selected Icon.
			*/
			
			g.setColor(Color.white);
			String s = SlowVM.toString(icon.ob);
			g.drawString(s, (int)icon.x, (int)icon.y);
			
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
		
		//TODO in general need to be able to call a thing on itself,
		//so I might want a button for that instead of having 2 copies of each thing.
		//But I can create duplicates by building an {{s k} k}, which I did,
		//but havent saved any of it yet after the program starts.
		
		IconsPanel ui = new IconsPanel(icons,(JComponent j)->{new JLabel("(Empty)");});
		JFrame window = new JFrame("IotaDesktop");
		window.setSize(500, 500);
		window.setLocation(200, 200);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(ui);
		window.setVisible(true);
	}

}