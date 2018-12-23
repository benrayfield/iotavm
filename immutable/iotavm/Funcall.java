/** Ben F Rayfield offers this software opensource MIT license */
package immutable.iotaVM;
import static immutable.iotaVM.Leaf.*;
import static immutable.iotaVM.Optimize.*;

import java.util.logging.Level;

/** UPDATE: I'm using object pointers (instead of longs) since I expect java is
better optimized for jumping object pointers than looking up longs,
especially when the class is final (which I tested in wavetree software
and got about 10 million objects created per second on a 4x1.6ghzLaptop).
<br><br>
long data includes isret bit and which kind of leaf (iota or reflect)
and up to 32 bits (always a powOf2 size) of literal data
whose childs are {{cons highBits} lowBits} and has optimizations
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
<br><br>
How a pure function can interact with stateful systems without itself becoming stateful:
A pure function is a kind of number. You cant change 5. If you add 2, you get 7. 5 is not 7.
Similarly, everything "done to" or "done by" a pure function is another function
(or eval step before returning). If you send a message to a pure function, it will
never see it. So how can stateful systems, such as clouds or mouse and keyboard,
interact with a pure function?
A pure function can return, or contain somewhere inside itself, something interpreted
as a request, which an external program may observe as a fact that the number/pure-function
contains, then fork-edit the pure function, creating a different pure function which
contains a response (which is itself pure), then call the forkEdited form. The pure
function still exists before and after the response as separate objects.
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
	//public final long dataAndOrId;
	
	public final long idAndMask;
	
	/** Does there need to be this int separate from long idAndMask,
	for optimizing switch case statements in javassist compiled code?
	I wouldnt want to force the idAndMask to choose which max 2^31 of them to optimize
	cuz which to optimize might change over time.
	*/
	public final int switchOptimize;
	
	/** optional extended data instead of just dataAndOrId,
	such as 4096 bits in an int array instead having to store that in 512 objects.
	This ust always be a powOf2 num of bits or null. This is usually null.
	<br><br>
	Left/func child is first half of array. Right/param child is second half.
	Those would normally not be called since this is an optimization which does it the slow way if so.
	*
	public final Object array;
	*/
	
	public final Blob blobOrNull;
	
	//FIXME do I want an int offset and int size into array as a separate object so it can be shared?
			
	//FIXME do I want forkap optimization?
	
	/*FIXME do I want noniota funcs in the forest, for iota to be controlflow between?
	That could fix much of the difficulty of having to rebuild things as ints
	and derive the basic int math. All of that, I could do, but its hard to optimize.
	*/
	
	/** Until get the system working with no optimizations, dont use (long)dataAndOrId
	for anything except the isRet mask and anything directly required by leafs.
	TODO true.
	*/
	public static final boolean enableOptimizations = false;
	
	/** TODO KEEP THIS COMMENT, BUT AM MERGING long data and long id,
	cuz if func and param are nonnull, then there is no literal data,
	so most of long data can be the id.
	<br><br>
	If func and param are null, then data==id. Else id is local to this VM
	and refers to this specific java object but does not necessarily refer to
	the unique binary forest shape since dedup is expensive and optional per node.
	Id is 64 bits so you can use them longterm over years, at each moment defining
	the id as the number of nanoseconds since year 1970 UTC (local ids, use dedup for global),
	so you get a billion potential ids per second, of which you'll probably use
	a few million. Global names would be by some kind of secureHash.
	*
	public final long id;
	*/
	
	/** This is not equal to nil which is defined as a certain combo of iotas as in the church encoding of lambda.
	This is an object only to be used within the VM and must never reach user level code
	and this behavior can be emulated only by knowing the long dataAndOrId.
	This is an optimization to allow the use of func and param instancevars without checking for null.
	*/
	public static final Funcall NULL = new Funcall();
	
	/** only allowed within the VM as an optimization. User level code can emulate the VM without
	needing this object.
	*/
	public boolean isNull(){
		return this==NULL;
	}
	
	/** This is the NULL funcall, which is a replacement for func and param being null
	and must only be used within VM. Theres only 1 instance, called NULL.
	*/
	private Funcall(){
		this.func = this;
		this.param = this;
		this.idAndMask = mask_isRet;
		this.blobOrNull = null;
		this.switchOptimize = 0;
	}
	
	public Funcall(Funcall func, Funcall param, long idAndMask, Blob blobOrNull, int switchOptimize){
		this.func = func;
		this.param = param;
		this.idAndMask = idAndMask;
		this.blobOrNull = blobOrNull;
		this.switchOptimize = switchOptimize;
	}
	
	public static Funcall leaf(int leafConstant){
		throw new Error("TODO");
	}
	
	/** Everything is a literal or id.
	<br><br>
	Negative longs are literals, self contained funcs made of iota such as
	an int32 is a complete binary tree whose leafs are car (1) and cdr (0),
	and which has long values for the {cons highBitsOfIntHalfSize} and 
	{{cons highBitsOfIntHalfSize} lowBitsOfIntHalfSize} of each branch in that complete binary tree,
	so its self contained all the way down to the cars and cdrs without allocating objects.
	<br><br>
	Nonnegative longs are ids, normally number of nanoseconds since year 1970 UTC
	(local ids, use dedup for globa).
	*/
	static final long mask_isLiteral = 1L<<63;
	
	static final long mask_isRet = 1L<<62;
	
	public Funcall lazyCall(Funcall param){
		if(enableOptimizations){
			if(func == null){
				//this is a wrapper for long dataAndOrId
				//such as an int is a complete binary tree of car or cdr leafs (or int16 inside that etc).
				
				//TODO I havent decided on which flags go in dataAndOrId for int32 int16 int8 and
				//their {cons highBitsOfIntHalfSize} vs {{cons highBitsOfIntHalfSize} lowBitsOfIntHalfSize}
				
				//First, decide what all flags I need.
				//I want all positive longs to be ids (high bit is 0), normally nanaoseconds since year 1970.
				//If high bit is 1, then its self contained, a func made of only iotas.
				//The only such optimization I need to get started is the ints,
				//so lets do just that for now, allowing for changing the long data format in future VMs
				//as long as they do the same core ops.
				
				//dataAndOrId must be negative here, since if it were positive, func!=null and param!=null.
				
				//See Optimize class constants.
	
				if(param.func == null){ //both are wrappers for long dataAndOrId
					//If they are both wrappers of the same size integer,
					//return another wrapper of integer with twice as many bits
					//(if they're not max wrappable int size), else return a normal pair.
					//if(same size integer and fits){
					//	TODO
					//}
					throw new Error("TODO");
				}
				
				//long newDataAndOrId = TODO;
				//return new Funcall(this, param, newDataAndOrId);
				throw new Error("TODO");
				
			}else{
				//long newDataAndOrId = TODO;
				//return new Funcall(this, param, newDataAndOrId);
				throw new Error("TODO");
			}
		}else{
			long data = 0;
			//leave mask_isRet as 0 aka LAZYEVAL.
			
			//FIXME theres a few things could check if its already trivially returned,
			//such as Sxy is a RETURN if x and y are RETURNs, unlike Sxyz which is always LAZYEVAL.
			throw new Error("TODO");
			//return new Funcall(this,param,data);
		}
	}
	
	/** the Leaf.evalStepMyCostIsAtMostForestDepth op.
	It can call any of the other ops in turingComplete combos if called in a loop
	but since thats an eval/debugger step it always halts (in at most forest depth, often less).
	*/
	public Funcall evalStep(){
		if(enableOptimizations){
			throw new Error("TODO");
		}else{
			/*First thing evalStep should do is recurse to find an isRet
			(func/left first, then look in param/right, so depthFirst order),
			go back a recursion, eval there, then pair recursively up to where started.
			This will be just a small constant number of pairs if emulating the call stack with iotas
			but thats not done here, thats done at user level and can efficiently emulate whats done here.
			Every evalStep evals one tiny thing at variable depth (up to at worst forest depth)
			then pairs back up to where it started, so its actually MORE efficient for iotavm
			to emulate the stack part of what this func does (than this func is efficient cuz of the depth),
			the first emulation, but would be less efficient
			for emulating the emulator, and so on (which it can).
			Such emulation is an efficiently forkEditable linkedlist of pairs (by lazyEval cons)
			of node to evalStep and car or cdr tells which direction it went into child.
			*/
			
			
			if(isRet()) return this; //true if isLeaf or some other cases
			int high = (int)(idAndMask>>>32);
					
			
			/*Must dedup the small optimized constant objects, such as s, k, and cons,
			since they are each a binary forest of iotas, and wont recognize them
			and know what logic to do to make the system work at all.
			Its not an optimization to process S, Sx, Sxy, Sxyz (becomes {{xz}{yz}})
			as iotas. Thats needed to use iota as binary forest without lambda param names.
			So use the Funcall object constants for those.
			But if create an equal binary forest, it needs to dedup to those constants.
			Maybe there should be masks for S, Sx, Sxy, Sxyz and similar for K and for Iota
			and those masks created in lazyCall?
			*/
			
			switch(high){
			case leafIota: case s: case carAkaKAkaTrue: case leafReflect:
				return this;
			
			/*THIS IS WRONG, CUZ THIS IS WHAT SHOULD HAPPEN IF this.func is leafReflect.
			case leafReflect:
				if(func==null){ //leaf (only true cuz !enableOptimizations)
					//Must return a pair of func and param that when called returns this.
					//Nondeterministic (may vary by VM) but one such func is {{k this} k},
					//but I'll return a more intuitive call {identityFunc this}.
					return theCons.lazyCall(theIdentityFunc).lazyCall(this);
				}else{
					return theCons.lazyCall(func).lazyCall(param);
				}
			*/
			default:
				throw new Error("TODO");
			}
		}
	}
	
	private boolean isLeaf(){
		throw new Error("TODO");
	}
	
	private boolean isIdentityFunc(){
		if(enableOptimizations){
			throw new Error("TODO");
		}else{
			/*DONE, but still need to adjust other funcs to check for NULL.
			FIXME create a NULL Funcall so func and param childs can still implement funcs,
			but it must be knowable from the long dataAndOrId if it is such NULL.
			The childs of NULL are NULL and NULL.
			*/
			return func==theIota && param==theIota;
		}
	}
	
	private boolean isKAkaCarAkaTrue(){
		if(enableOptimizations){
			throw new Error("TODO");
		}else{
			return func==theIota && param.func==theIota && param.param.isIdentityFunc();
		}
	}
	
	private boolean isS(){
		if(enableOptimizations){
			throw new Error("TODO");
		}else{
			return func==theIota && param.isKAkaCarAkaTrue();
		}
	}
	
	/** Use at your own risk of nonhalting. Halting-oracles are impossible, and this waits for halt.
	Its recommended to use evalStep instead, which this calls in a loop.
	*/
	public Funcall evalOrCrash(){
		Funcall call = this;
		while(!call.isRet()) call = call.evalStep();
		return call;
	}

	/** Similar to evalOrCrash but with a limit on the number of evalSteps(),
	(FIXME) but this may not be the most efficient way since this does not call optimizations
	such as opencl or javassist. This calls evalStep literally.
	*/
	public Funcall evalLimitElseNull(int maxSteps){
		Funcall call = this;
		while(!call.isRet() && maxSteps-- > 0) call = call.evalStep();
		if(maxSteps <= 0) return null; //FIXME off by 1?
		return call;
	}
	
	/** true if this is a RETURN, else is a LAZYEVAL.
	<br><br> 
	Funcall f = something;
	Funcall call = f.lazyCall(anything);
	while(!isRet(call.)) call = call.evalStep();
	return call;
	//TODO opcode to jump ahead many steps within int max cost
	//of Funcall steps or cheaper where optimized.
	<br><br>
	Param is dataAndOrId instead of using this.dataAndOrId cuz complete binary trees of car and cdr
	as big as int32 can be represented as a long without a Funcall object,
	as iotas, and so can all their parts.
	*/
	public static boolean isRet(long dataAndOrId){
		return (dataAndOrId&mask_isRet)!=0;
	}
	
	public boolean isRet(){
		return isRet(idAndMask);
	}
	
	/** UPDATE: SlowVM.c and SlowVM.cShorter funcs do this but without optimizations yet.
	<br><br>
	compiles lambda strings like "La.a(Lb.Lc.Ld.bd(cd))(Le.Lf.e)" to iotas,
	where var names must be a single nonwhitespace char thats not L, ., (, or ).
	TODO is that really the syntax I want?
	Or how about this syntax?: a.a(b.c.d.bd(cd))(e.f.e)
	Also how about using ` like in unlambda, which means ( and doesnt have ) cuz its automatic?...
	First, make it a binary forest: a.(a(b.c.d.bd(cd)))(e.f.e)...
	(La.a(Lb.Lc.Ld.bd(cd))(Le.Lf.e))
	(a.``a(b.c.d.``bd`cd)(e.f.e))
	La.``aLb.Lc.Ld.``bd`cdLe.Lf.e
	La``aLbLcLd``bd`cdLeLfe
	Replace L with ^ which is how unlambda writes lambda funcs:
	^a``a^b^c^d``bd`cd^e^fe
	Or how about capital letter defines the var starts the lambda, and lowercase letter gets the var:
	A``aBCD``bd`cdEFe
	...
	YES, thats a simpler syntax than
	La.a(Lb.Lc.Ld.bd(cd))(Le.Lf.e)
	...
	Lets replace ` with $ so it can be a var in common langs:
	A$$aBCD$$bd$cdEFe
	<br><br>
	BTW that string compiles to iota, but many functions do the exact same thing but
	that cant be proven, so dont expect a dedup of all possible functions.
	The dedupStep leaf/coreFunc is only for binary forest shape, not func behaviors.
	<br><br>
	If you want to inline existing Funcalls, curry an extra param for each
	then lazyCall on each as the next param. 
	<br><br>
	The number of iotas returned is exponential of number of curries,
	which you should try to keep small and use linkedlists etc instead
	when more params are needed. Remember, a linkedlist is made of iotas, like everything else.
	<br><br>
	OLD CONFUSING COMMENT WRITTEN SOMEWHERE ELSE THAT LED TO WANTING TO CREATE THIS FUNC...
	That way of currying n params with iota is to derive S and K and curry the same way
	its often done in unlambda. SKK is identity func and gets the param. Kx quotes x.
	Sfp is func call of func f on param p, and Sfpz passes param z recursively
	in that binary forest of func calls. Thats how to curry 1 param.
	To curry another param, you build a func that returns all that EXCEPT in one place
	it returns identityFunc {iota iota} (not skk which is also identityFunc but not the normed form).
	instead of returning something that returns identityFunc.
	To curry a third param, do the same thing as for the second, and so on.
	You use s and k to create a func that returns a call of s.
	*/
	public static Funcall lambdaStringToIotas(String code){
		throw new Error("TODO");
	}
	
	//only funcs you need are public.
	//If you need a constant for lispCons, lispCar, and lispCdr, which are all derived from iota,
	//then derive them from iota and they will be automatically optimized since the VM checks for
	//that forest shape. There is a standard way to define currying of n params with iota,
	//and it costs exponential number of iotas per linear bigger n, so try to keep the number
	//of curried params low and instead pass it a linkedlist or other datastruct.
	//We do still need a little currying to derive basic things from (will be optimized in VM).
	//That way of curring is explained in lambdaStringToIotas func. 
	
	public static final Funcall theIota = leaf(leafIota);
	
	public static final Funcall theReflect = leaf(leafReflect);
	
	
	
	private static final Funcall theIdentityFunc = theIota.lazyCall(theIota).evalOrCrash();
	
	private static final Funcall theKAkaCar = theIota.lazyCall(theIota.lazyCall(theIdentityFunc)).evalOrCrash();
	
	private static final Funcall theS = theIota.lazyCall(theKAkaCar).evalOrCrash();
	
	private static final Funcall theCons = null; //FIXME
	
	private static final Funcall theCdr = null; //FIXME
	
	/*Funcall func(){
		if(func!=null) return func;
		throw new Error("TODO derive it from dataAndOrId as a literal 1 2 4 8 16 or 32 bits etc.");
		FIXME what should be the func and param values for leafs? Themself?
		It must be a func and param that when called returns that leaf,
		so it could be {{k leaf} anything}.
	}
	
	Funcall param(){
		if(param!=null) return param;
		throw new Error("TODO derive it from dataAndOrId as a literal 1 2 4 8 16 or 32 bits etc.");
		FIXME what should be the func and param values for leafs?
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
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
	
	
	/*
	 * I want the long dataAndOrId simplified by not storing literal data in it
	and instead only using it for bitmasks and sizes etc.
	Put the literal data in "Object array" field instead,
	even if its just an Integer or int[] (or wrapper of int[] and offset and len) etc.
	The only thing required to dedup is the wrapping of literal data (always powOf2
	num of bits as car and cdr). The rest can be run as iotas or optimized on a best effort basis.
	
	
	
	
	To make the reflect op deterministic,
	every binary forest node must have a well defined func/left and param/right.
	The only nodes which dont are the leafs.
	Since the call of func/left on param/right is defined as equal to their parent,
	define the 2 childs of every leaf x as identityFunc={iota iota} and x.
	
	Should the dedup func be derived from iotas?
	
	Bit fields needed per node without optimizations:
	isRet
	isDeduped
	
	Do I really need access to dedup from user level code?
	weakEquals on any 2 objects that have true isRet will work for leafs/coreOps.
	In an abstract math way, but not in an optimization way,
	all I really need is field isRet and pairIsEqualLeafs leaf/coreOp.
	From that, it is technically possible to derive various merkle hashes
	(as funcs made of iotas and isRet etc)
	but the caching of them would be the issue, and to optimize that for certain known hashes
	to call java MessageDigest of "SHA256" forExample
	(which returns in constant time, though expensive constant).
	*/

}
