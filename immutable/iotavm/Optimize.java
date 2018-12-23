/** Ben F Rayfield offers this software opensource MIT license */
package immutable.iotaVM;

/** These are part of (long)Funcall.dataAndOrId data format which is not part of the iotavm spec.
The iotavm spec is defined only by the Leaf constants and all possible binary forests of them
and evalStep of each forest returns another such forest.
These constants are optimizations of that, such as an int32 is a complete binary tree
whose leafs are car and cdr, and has {cons highBitsOfIntHalfSize} and
{{cons highBitsOfIntHalfSize} lowBitsOfIntHalfSize} forms
of each size int2 int4 int8 int16 int32, so those funcs, all made only of iotas,
can be used efficiently as ints shorts bytes etc without allocating objects in most cases,
and later opencl optimizations are planned.
<br><br>
The optimization of iotavm is a very hard problem that many people can work together on
within the spec defined in the Leaf core ops and everything being a binary forest as a func.
<br><br>
All these constants go in the high 32 bits of (long)Funcall.dataAndOrId.
The low 32 bits are either the low 32 bits of an id or contain literal data for int1 to int32.
<br><br>
FIXME these constants dont always have the masks in them, such as isLiteral and isRet.
<br><br>
The ((Enum)Leaf).ordinal() must be included in these constants, upward from 0.
*/
public class Optimize extends Leaf{
	private Optimize(){}
	
	//public static final int int1 = 0;
	
	//TODO funcs for calling into datastruct outside Funcall class that stores
	//binheap indexed bitstring of powOf2 number of bits, of course immutable once observed.
	//Thats the natural extension of int1 to int32. Its int64, int128, ... up to maybe int65536.

	
	/** In these names, Call means the same as ` in unlambda. */
	public static final int int32CallCallConsHighLow = 201;	
	public static final int int32CallConsHigh = 200;
	public static final int int16CallCallConsHighLow = 199;
	public static final int int16CallConsHigh = 198;
	public static final int int8CallCallConsHighLow = 197;
	public static final int int8CallConsHigh = 196;
	public static final int int4CallCallConsHighLow = 195;
	public static final int int4CallConsHigh = 194;
	public static final int int2CallCallConsHighLow = 193; 
	public static final int int2CallConsHigh = 192;
	
	
	/** La.Lb.Lc.ac(bc). The k lambda is called carAkaKAkaTrue. */
	public static final int s = 132;
	
	/** La.a aka {iota iota} */
	public static final int identityFunc = 131;
	
	/** La.Lb.Lc.cab */
	public static final int cons = 130;
	
	/** La.Lb.a
	Moving all these up, cuz the ((Enum)Leaf).ordinal() will be first.
	OLD: lispCar in the church encoding of lambda is True so makes sense for its optimize constant to be 1.
	OLD: lispCdr in the church encoding of lambda is False so makes sense for its optimize constant to be 0
	*/
	public static final int carAkaKAkaTrue = 129;
	
	/** La.Lb.b */
	public static final int cdrAkaFalse = 128;
	
	
	//The iotavm spec only defines the leafs (below),
	//not the optimization constants (above) derived from combos of the leafs.
	//From each optimization constant you can leafReflect
	//to navigate that binary forest down to such leafs.
	
	
	
	/* inherit these instead of copy.
	
	public static final int leafIota = Leaf.iota.ordinal();
		
	public static final int leafReflect = Leaf.reflect.ordinal();
	
	public static final int leafIsRet = Leaf.isRet.ordinal();
	
	public static final int leafEvalStepMyCostIsAtMostForestDepth = Leaf.evalStepMyCostIsAtMostForestDepth.ordinal();
	
	public static final int leafEvalStepLimitDepthTo8 = Leaf.evalStepLimitDepthTo8.ordinal();
	
	public static final int leafWeakEquals = Leaf.weakEquals.ordinal();

	public static final int leafDedupStep = Leaf.dedupStep.ordinal();
	
	public static final int leafIsDeduped = Leaf.isDeduped.ordinal();
	*/

}
