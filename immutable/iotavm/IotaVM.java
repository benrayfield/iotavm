/** Ben F Rayfield offers this software opensource MIT license */
package immutable.iotavm;

/** An immutable forkEditable merkleAble binary forest where every leaf is one of (enum)Leaf,
and the only allowed op is to call one func on another func which returns a func,
and some internal garbcol stuff etc. Every such func is a long,
and most longs are self contained small datastructs instead of pointing to the heap.
*/
public interface IotaVM{
	
	//TODO use Funcall (which contains a long) instead of long?
	
	//A binary forest where every leaf is one of IotaFunc.
	
	public long leaf(Leaf f);
	
	/** This returns in constant time, often in less than a microsecond,
	either by it being a very simple calculation or by returning a LAZYEVAL.
	<br><br>
	There are no literal pairs except funcs which act like pairs.
	A pair is a func that calls its param on myCar then what that returns is called on myCdr,
	and that pair is made of iotas if its observed other ways than through car and cdr,
	which are themselves made of iotas. Even the debugger steps are made of iotas.).
	*/
	public long funcall(long func, long param);
	
	/** garbage collects everything except whats reachable from the param in the binary forest.
	This only makes it available for a lower system (such as JVM or Opencl etc) to garbcol
	which may happen later and must automatically happen before run out of memory.
	*/
	public void keepOnly(long func);
	
	
	
	
	/** gets the bit value of a func, if its a complete binary tree whose leafs are car and cdr,
	which means its either a car or a cdr.
	*/
	public boolean unwrapZ(long func);
	
	/** gets the byte value of a func, if its a complete binary tree whose leafs are car and cdr */
	public byte unwrapB(long func);
	
	/** gets the short value of a func, if its a complete binary tree whose leafs are car and cdr */
	public short unwrapS(long func);
	
	/** gets the char value of a func, if its a complete binary tree whose leafs are car and cdr */
	public default char unwrapC(long func){ return (char)unwrapS(func); }
	
	/** gets the int value of a func, if its a complete binary tree whose leafs are car and cdr */
	public int unwrapI(long func);
	
	/** gets the long value of a func, if its a complete binary tree whose leafs are car and cdr */
	public long unwrapJ(long func);
	
	/** If this is one of the optimized types, returns the object it wraps */
	public Object unwrapL(long func);
	
	public long wrapL(Object o);
	
	public long wrapJ(long integer);
	
	public long wrapI(int integer);
	
	public long wrapS(short integer);
	
	public default long wrapC(char integer){ return wrapS((short)integer); }
	
	public long wrapB(byte integer);
	
	public long wrapZ(boolean bit);

}
