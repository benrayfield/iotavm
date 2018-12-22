/** Ben F Rayfield offers this software opensource MIT license */
package immutable.iotavm;

/** dense sharable subarrayable array of a powOf2 number of bits, OR NULL.
If nonnull, represents a complete binary tree with car and cdr as leafs,
and (TODO) has an optimized representation of {{cons highBits} lowBits} and {cons highBits}
separately at each level of recursion.
<br><br>
WARNING: try to keep these arrays not too big cuz they do prevent garbcol when
recursing smaller into them then holding ptr, but thats to be optimized by iotavm.
*/
public final class Blob{
	
	/** bit indexs. from (inclusive) to to (exclusive). from and to always choose
	a powOf2 size range thats binary searchable from the outermost array size.
	*/
	public final int from, to;
	
	public final long[] array;
	
	/*FIXME where to store the "has an optimized representation of {{cons highBits} lowBits} and {cons highBits}
	separately at each level of recursion."?
	FIXME I dont know if this field answers the question or how and when to transition between its values by forkEdit.
	*/
	public final boolean isConsOfHighbits;
	
	public Blob(long[] array, int from, int to, boolean isConsOfHighbits){
		this.array = array;
		this.from = from;
		this.to = to;
		this.isConsOfHighbits = isConsOfHighbits;
	}
	
	public Blob left(){
		//FIXME only if is not already a single bit
		if(isConsOfHighbits){
			throw new Error("FIXME is this cons or highbits or lowbits or what?");
		}else{
			return new Blob(array, from, (from+to)/2, true);
		}
	}
	
	public Blob right(){
		//FIXME only if is not already a single bit
		if(isConsOfHighbits){
			throw new Error("FIXME is this cons or highbits or lowbits or what?");
		}else{
			return new Blob(array, (from+to)/2, to, isConsOfHighbits);
		}
	}

}
