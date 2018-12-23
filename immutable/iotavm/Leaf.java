/** Ben F Rayfield offers this software opensource MIT license */
package immutable.iotaVM;

/** The main core op is [iota=Lf.f(La.Lb.Lc.ac(bc))(Ld.Le.d)=Lf.fSK].
Since lambda funcs lack the ability to look at the sourceCode of lambda funcs without calling them,
I'm creating a core func f that returns a pair of any func and param that would return x, when given param x,
such as {f {itsCar itsCdr}} may return {{cons itsCar} itsCdr} which is a structure made of iotas,
or such as {f {itsCar itsCdr}} may return {{cons {k {itsCar itsCdr}}} anything},
so it is nondeterministic, but its needed to implement the iota VM as iota funcs
while allowing emulation of emulation of emulation as deep as you want
(exponentially expensive for linear deeper) but the point is its more consistent math.
<br><br>
Debugger steps will be done using a linkedlist of pairs which each go left or right into
the binary forest whose leafs are core ops. This binary forest will have at each node
either car or cdr func to tell which direction it went.
<br><br>
Each node in binary forest will have a bit that tells if its a RETURNED value vs a LAZYEVAL,
which can be determined by the shape of the tree, since Sxy is a RETURN if x and y are both RETURNs,
but Sxyz is a LAZYEVAL, forExample. {{{Sx}y}z} becomes {{xz}{yz}} which is a LAZYEVAL.
<br><br>
There may need to be a core op to check if something is a RETURN vs LAZYEVAL?
I hope not, cuz if it can be determined by forest shape, should name that func one of the
forests of iotas and "core func f that returns a pair of any func and param
that would return x, when given param x" and just optimize it when called instead of actually
running every step of it.
<br><br>
Dedup is not guaranteed but is allowed to happen automaticly by lazyEvalHash.
<br><br>
Every object is named by a long whose low 32 bits are often used as a literal value,
and high 32 bits contain bit masks and other internal details of the VM.
These will for efficiency often refer to binheap addressed binary blobs of any powOf2 size
and whose leafs are car or cdr, and be used with a linkedlist of cars with a cdr at end
(or of cdrs with a car at end) of same depth as that completeBinaryTree.
There will be an extra bit of that address space that tells if you mean
{{cons aCar} aCdr} or {cons aCar}, and that applies recursively,
so as long as you dont trigger eval of {{cons aCar} aCdr} or {cons aCar}
which are both LAZYEVALs, you can navigate complete binary trees of bits
and create pointers to them, all without creating objects on the java heap
and with 100%-epsilon efficiency of binary storage, where the epsilon
is the few Funcall objects needed to find and optimize it.inheap
Binheap doesnt use first index, but only the high half of indexes are stored,
and the rest abstractly refer to powOf2 sized parts of the array.
There may be some other metadata to store in
such a binheap such as does it have an expire time to garbcol or econacyc numbers
or whatever might be added in VM.
<br><br>
Only integers are allowed, no floats since they are often nondeterministic especially in opencl.
But we can derive for example powers of e using integers for a few times the compute cost.
<br><br>
Optimizations of func calls of core ops will use native code such as opencl or javassist,
such as using them to multiple 2 ints which are abstractly represented as binary forests
of car and cdr leafs, but you dont have to eval them as forests if you know the func
multiplies int of 2 binary forests and returns another binary forest.
There will be a few similar optimizations.
<br><br>
Any optimization is allowed as long as it gives the exact same behaviors,
except allowing nondeterminism in "core func f that returns a pair of any func and param
that would return x, when given param x" which is at least deterministic
in that it returns a pair of the left and right childs of a LAZYEVAL,
but we just cant tell which lazyeval it is that all return the same thing,
except we can tell by calling "core func f that returns a pair of any func and param
that would return x, when given param x". The problem is, to be a pure math func,
the system is not allowed to know which of those lazyevals it is. But it will.
<br><br>
A pair is always function call. There are no literal pairs except by function calling
{{cons aCar} aCdr}.
*/
public class Leaf{
	Leaf(){}
	
	
	/*The bitstring of powOf2 size of bits as complete binary tree of car and cdr (efficient binary memory)
	is the ONLY part that has to optimize reliably. The rest of things can sometimes execute as iotas
	and other times execute in optimized form, and it will either way either work
	or run out of compute resources. Remember, if you want to multiply 2 ints you have to derive
	the int multiply operator on complete binary trees of car and cdr and check for that,
	or let it happen as iotas without that optimization (which would be many times slower).
	AND I need the complete binary trees to get big enough that its efficient to store neuralnet
	weights there, such as 1-4096 bits (such as 64 longs) each
	(which it must rarely copy to half that size cuz is inefficient).
	*/
	
	/*TODO should there be only 1 leaf (so reflect is simpler) and define certain small forests
	as the core ops instead, similar to in binacyc32 I used {{{}{}}{}} as bit1 and {{}{{}{}}} as bit0?
	Their parts would have to be funcs. Maybe those parts always return themself?
	I need consistency, that the one leaf is a certain func, and calling it on itself is {{}{}},
	and so on for other binary forests of it.
	What is that core op (which can do iota, reflect, isret, etc)?
	*/
	
	
	/** Lf.f(La.Lb.Lc.ac(bc))(Ld.Le.d) aka Lf.fSK */
	public static final int leafIota = 0;
	
	/** this is the "core func f that returns a pair of any func and param
	that would return x, when given param x" (see comment of this class).
	"such as {f {itsCar itsCdr}} may return {{cons itsCar} itsCdr} which is a structure made of iotas,
	or such as {f {itsCar itsCdr}} may return {{k {itsCar itsCdr}} anything},"
	*/
	public static final int leafReflect = 1;
	
	/** Returns car if its param is a RETURN, else returns cdr since its param is a LAZYEVAL.
	FIXME If this can be derived from binary forest structure, then do that instead of
	complicating the system by making this a core op, and optimize calls of it.
	*/
	public static final int leafIsRet = 2;
	
	/** This is the only leaf/coreOp that does not always return in constant time,
	but it does always return (in at the longest the forest depth, and can be used in ways that prove its always constant time).
	<br><br>
	A debugger-step/stack-step of triggering lazyEval recursively.
	For example, changes  {someOtherStuffRecursivelyUp...{{{Sx}y}z}} to {someOtherStuffRecursivelyUp...{{xz}{yz}}},
	and we know that x y and z are not all LAZYEVALS (some kind of optimizations similar to unlambda andOr lazyk languages?)
	cuz if it was then would have recursed into those instead of evaling here.
	Remember that S is derived from iotas (is it {iota {iota {iota {iota iota}}}} or something like that?).
	<br><br>
	Always returns in at most as many steps as forest depth, depending on isRet of a path of childs recursively.
	This triggers eval of lazyEval in this order, but only 1 of these paths: left/func child, right/param child, or self,
	and then after doing that tiny piece of work, forkEdits all parents upward to param, to have the evaled form (!isRet) somewhere
	deeper inside. Or if this.isRet then returns this without needing to do any more work.
	This could be done more efficiently by using java stack, but it can be practically efficient by keeping the depth it must
	recurse small, such as by using a linkedlist (made of conses which are derived from iotas) of child of child of child,
	alternating index in linkedlist with car or cdr to tell which way it recursed, and guaranteed constant time eval only the top
	and a constant small depth below, returning the next stack state. This evalStep leaf/coreOp can be used to derive such a func,
	and to jump debugger levels, to debug the debugger, and debug the debugging of the debugger, and so on,
	always returning in "at most as many steps as forest depth" and on average returning in small constant steps.
	*/
	public static final int leafEvalStepMyCostIsAtMostForestDepth = 3;
	
	/** TODO once I figure out how to use evalStep efficiently,
	derive stack/debugger steps using this instead, guaranteeing that every op returns in constant time
	and the system is capable of jumping pointers for some small power of log cost,
	and can debug the debugger debugging debugger to any depth.
	*
	public static final int leafEvalStepLimitDepthTo8 = 4;
	*/
	
	/** FIXME this should take {{cons x} y} as param instead of currying more than 1 param,
	since every other core op takes 1 param and selfModifies by forkEdit (except in certain optimizations)
	to not necessarily store the earlier curried params.
	<br><br>
	Depending on which I choose of the FIXME(s) below, this will be named weakEquals or do2LeafsEqual.
	Returns car for true, cdr for false.
	This is the java operator == for core ops such as iota, reflect, and (itself) do2LeafsEqual.
	Example: {{do2LeafsEqual iota} iota} returns car.
	Example: {{do2LeafsEqual iota} do2LeafsEqual} returns cdr.
	Example: {{do2LeafsEqual {iota iota}} {iota iota}} returns cdr since those are not leafs/coreOps,
	even though (FIXME this could cause problems) they may eval to it.
	Remember, car, cdr, and cons are derived as binary forest of iotas.
	If this could compare more than leafs, it could cause problems.
	Maybe I should just allow it on every object instead of trying to verify they're leafs,
	since requires haltingOracle to prove a LAZYEVAL is or is not (RETURNs) a leaf, in some cases.
	*
	public static final int leafWeakEquals = 5;
	//do2LeafsEqual
	*/
	public static final int leafIsConsOf2Leafs = 5;
	
	//TODO derive isLeaf func using weakEquals comparing it to every known leaf/coreOp,
	//then optimize that func to not use the iotas its derived from but still name it that way.
	
	/** If both childs of the param are deduped (which depends on if they're null andOr long dataAndOrId)
	then returns a deduped form of those 2 childs and their dataAndOrId,
	BUT if either (or both) childs are not deduped, then returns param
	(which may confusingly be expected to be the normed/deduped form) so make sure to check
	if both childs are deduped (or in some cases optimized way to check is the dataAndOrId)
	before calling this.
	<br><br>
	The purpose of this being a STEP is to always return in constant time and require the recursion
	be done in terms of coreOps. The VM must always return in constant time,
	and its only turingComplete when run in a loop feeding its output to its input.
	<br><br>
	After verifying both childs are deduped (which usually requires recursion of iotas etc)
	the dedupStep of them weakEquals the dedupStep of anything else by that process
	which has the same forest shape and coreOps/leafs.
	*
	public static final int leafDedupStep = 6;
	
	TODO should leafDedupStep be replaced with a variable depth (at worst forest depth) leafDedupVarDepthStep?
	If so you would call leafDedupStep in a loop until leafIsDeduped
	and could use it the same way except if both childs are not deduped then instead of
	returning the param thats not deduped it does a tiny amount of the work inefficiently.
	Its still most efficient to use it without such recursion by emulating the stack and calling this from that.
	...
	If so update comments that say anything else is the only core op thats not constant cost.
	
	/** what should be called on both childs (or in some cases dataAndOrId) before dedupStep
	else recurse to dedup them, optionally cancelling that process if its too much work.
	Returns car for true or cdr for false.
	*
	public static final int leafIsDeduped = 7;
	*/
	
	/*Is this going too far???
	
	TODO derive dedup func, allowing various dedup funcs (such as sha256 or sha3-256 or arbitrary func,
	(which could be optimized to call java MessageDigest of "sha256" forExample,
	when that forest of iotas occurs, and on rare occasion compute it in iota form and compare results,
	will be equal else theres a VM bug)
	each with pre and post processing such as for isRet and optimized int1-int32 literals),
	and somehow optimize the caching of it, so its just as good as leafIsDeduped and leafDedupStep.
	The caching is the hard puzzle to solve.
	A 256 bit globalname/hashcode for example would be a complete binary tree of 256 leafs (not iotavm leafs)
	which are each car or cdr (a tree based bitstring, unlike a linkedlist of car and cdr kind of bitstring).
	This would be efficiently stored in binary, efficient as long as you dont recurse into it
	cuz its childs would each be a copy of 128 bits, and childs of that copy of 64 bits,
	down to car and cdr. Probably 192 bits is enough if it was a stronger hash
	(such as derived from random sequence (size enough to permutate each bit about 5*log(bits) times
	of random choices of the (2^3)! possible unitary funcs
	from 3 bits to 3 bits (as in 3sat being npcomplete) on random indexs within 2*bitsize bits
	which is param of 2 childs to hash and output is bitsize the hash,
	AND after that unitary transform, take the xor of the input and the output so its
	not forward computable or reverse computable efficiently and occupies about 1/e (TODO verify)
	fraction of the space of all possible hashcodes (loses about 2 bits).
	But standard hash funcs are more optimized than messing with individual bits.
	*/
	
	/** If theres a replacement for an object, returns it, else returns the param.
	Replacements are nondeterministic and are used for things like norming by secureHash
	or gradually becoming more normed as a group of computers or parts of the same computer
	gradually agree on a bunch of objects being equal to eachother.
	A LAZYEVAL may always be replaced with a RETURN which it would return,
	but be careful in returning the next LAZYEVAL in its execution since
	that sequence of compute steps could eventually loop back around
	to return a state from its own past, so it would diverge into more objects
	instead of converging toward a (gradually more) normed form which this is for.
	*
	replacement
	//public long replacement(long func);
	*/
	
	//FIXME how will the reflect func tell which core op its param is?
}
