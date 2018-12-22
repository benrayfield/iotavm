/** Ben F Rayfield offers this software opensource MIT license */
package immutable.slowIotaAndorBinufnodeVMJustToFindTheRightImmutableForestLogicToOptimize;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class Ns{
	
	public final WeakHashMap<Map,String> os = new WeakHashMap();
	
	public final WeakHashMap<String,WeakReference<Map>> so = new WeakHashMap();
	
	/** linear copy. This could be done as ufnode for constant cost, and all changes are log cost,
	but this is not the bottleneck since its only for interaction with Human as display names and string code.
	*/
	public Ns fork(){
		Ns f = new Ns();
		f.os.putAll(os);
		f.so.putAll(so);
		return f;
	}
	
	public void putBidirectionally(String name, Map o){
		os.put(o, name);
		so.put(name, new WeakReference(o));
	}
	
	/*public ob copyToOb(cx c){
		throw new Error("TODO");
	}*/

}
