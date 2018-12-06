/** Ben F Rayfield offers this software opensource MIT license */
package immutable.iotavm.impl;

/** UPDATE: I'm using object pointers (instead of longs) since I expect java is
better optimized for jumping object pointers than looking up longs,
especially when the class is final (which I tested in wavetree software
and got about 10 million objects created per second on a 4x1.6ghzLaptop).
<br><br>
long data includes isret bit and which kind of leaf (iota or reflect)
and up to 32 bits (always a powOf2 size) of literal data
whose childs are {{cons highBit} lowBits} and has optimizations
so Funcall objects dont need to be created to navigate that literal data
in most cases. Remember, cons, car, and cdr are binary forests with iota leafs.
<br><br>
OLD:
self pointer is what VM names this binary forest pair.
There are no literal pairs, since a pair is a func derived from iota: Lx.Ly.Lz.zxy,
and so are car and cdr derived from iota, and together (TODO) are optimized
so dont have to call those iotas in most cases.
A pair costs only 2 Funcalls stored in memory
(or costs nearly no extra storage or compute cycles
if is a binheap indexed bitstring optimization).
<br><br>
The whole system can be built with Funcalls only without using any optimizations
such as binheap indexed bitstrings and car cdr cons optimizations,
so before optimizing that, verify the system works with Funcall only,
by deriving all those (which will be the same binary forest either way, isret differing).
*/
public final class Funcall{
	
	/** func and param are either both null or both nonnull. If null, use "long data" optimizations
	to create (and dont cache, since this should happen rarely compared to using the long directly)
	the 2 child Funcalls, such as carring or cdring into a complete binary tree whose leafs
	are car or cdr as a representation of an optimizable bitstring of powOf2 size.
	*/
	public final Funcall func, param;
	
	/** 0 1 2 4 8 16 or 32 of the low bits are literal data. The high 32 bits are for VM flags etc.
	If func and param are null (always both or neither are null)
	then this long completely describes this object, and id==data.
	*/
	public final long dataAndOrId;
	
	/** TODO KEEP THIS COMMENT, BUT AM MERGING long data and long id,
	cuz if func and param are nonnull, then there is no literal data,
	so most of long data can be the id.
	<br><br>
	If func and param are null, then data==id. Else id is local to this VM
	and refers to this specific java object but does not necessarily refer to
	the unique binary forest shape since dedup is expensive and optional per node.
	Id is 64 bits so you can use them longterm over years, at each moment defining
	the id as the number of nanoseconds since year 1970,
	so you get a billion potential ids per second, of which you'll probably use
	a few million. Global names would be by some kind of secureHash.
	*
	public final long id;
	*/
	
	public Funcall(Funcall func, Funcall param, long dataAndOrId){
		this.func = func;
		this.param = param;
		this.dataAndOrId = dataAndOrId;
	}
	
	/*public final long self, func, param;
	
	//TODO? I dont want any mutable parts, cuz java goes much faster with a pure immutable class.
	//public long replacement;
	
	public final boolean isret;
	
	public Funcall(long self, long func, long param, boolean isret){
		this.self = self;
		//this.replacement = self;
		this.func = func;
		this.param = param;
		this.isret = isret;
	}*/

}
