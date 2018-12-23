/** Ben F Rayfield offers this software opensource MIT license */
package immutable.slowIotaVM.ui;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import immutable.slowIotaVM.SlowVM;

/** draggable icon representing an iotavm funcall/object.
This can have a prehopfieldName andOr visual icon.
Every iotavm funcall/object is stateless/immutable,
except for maybe converging statistics on memory and compute cycles.
The icon has stateful y and x position on screen etc.
<br><br>
IotaVM will be immutable.iotaVM.Funcall objects in future versions
but for now are Map objects without much optimizations in the SlowVM system.
*/
public class Icon{
	
	/** position in the 2d space */
	public double y, x;
	
	/** hashname of the iotavm funcall/object *
	public String hashname;
	*/
	
	/** iotavm funcall/object.
	The "name" key, if it exists, is prehopfieldName,
	and does not affect the global hashname. See comment in body of SlowVM.hash(Map).
	*/
	public final Map ob;
	
	/** visual of the icon as intARGB color,
	will be copied into a shared BufferedImage before painting on screen.
	Multiple Icon can share the same int[][].
	*/
	public int[][] pic;
	
	public Icon(Map ob, double y, double x, int[][] pic){
		this.ob = ob;
		this.y = y;
		this.x = x;
		this.pic = pic;
	}
	
	/** for saving state to be loaded later. This only includes hashname
	of the iotavm funcall/object, not all the binary forest below it,
	which is stored in a different system.
	*/
	public Map toJsonMap(){
		throw new Error("TODO");
	}
	
	/** When this Icon is dragged onto targetIcon,
	creates another Icon that you are now dragging (or similar keyboard controls).
	Normally thats a funcall currying 1 more param (and maybe evaling up to n steps),
	but it could also be UI controls for purposes outside the iotavm system
	such as to limit the number of compute steps or change colors of the window etc,
	but hopefully the iotavm objects will be so useful that the state of the window
	is also done that way eventually. So far 2018-12-22 iotavm is still buggy
	and this ui will help me further design it.
	*/
	public Icon onDragTo(Icon targetIcon){
		Map call = SlowVM.pair(ob,targetIcon.ob);
		Map ret = SlowVM.evalLimitSteps(call,1000); //FIXME more steps
		if(ret == null) return this; //FIXME return what if too many steps?
		return new Icon(ret, y, x, pic); //TODO different pic?
		//throw new Error("TODO");
	}
	
	/** creates a new ui to view the details of this icon,
	normally at the bottom of the IotaDesktop when this Icon is selected.
	*/
	public JComponent viewer(){
		String s = SlowVM.toString(ob); //FIXME this could get exponentially big for linear forest size, so end after some depth or size
		JTextArea t = new JTextArea(s);
		t.setEditable(false);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		return t;
	}
	
	public double distance(Icon i){
		double dy = y-i.y, dx = x-i.x;
		return Math.sqrt(dy*dy+dx*dx);
	}

}
