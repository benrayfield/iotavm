/** Ben F Rayfield offers this software opensource MIT license */
package immutable.slowIotaAndorBinufnodeVMJustToFindTheRightImmutableForestLogicToOptimize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import immutable.util.HashUtil;
import immutable.util.Text;

/** Rules for SlowVM design:
<br><br>
After solving this, I go back to iotavm andOr binufnode design,
some middle ground between them, to optimize the logic found here
as the new turingComplete pureImmutable glueCode of my software
(allowing for humanainetMultiTuringlevel such as some is still plain java
and others is made this pureImmutable way). 
<br><br>
No using the java stack at variable depth, instead do at most forestDepth
steps at a time (best if small constant depth) of forest nodes.
<br><br>
iotavm reflect op must still work, or some variety of it
upgraded for the "middle ground between iotavm and binufnode".
<br><br>
Instant dedup every time.
<br><br>
No garbcol.
<br><br>
Any constant can have a humanReadable string name (prehopfieldName).
<br><br>
No optimizations except in calling MessageDigest to get hashname.
Keep datastructs and logic as simple as possible while
looking for a consistent logic that solves
the problem of needing exponentially many iotas
for linear number of currys, solves it
somehow similar to binufnode ``curry
but without any other operators except
how I planned to derive all the operators
with iota then optimize them with VM hooks,
except I cant have a VM hook for nearly as
many funcs as need optimizing so I need
some datastruct similar to {currys evalMe}
but that has never been fully explored
to find the simplest consistent efficient variant of it.
<br><br>
All objects are java.util.Map.
All keys are String.
Key "this" maps to the sha256hex hashname
of this map (excluding the "this" key/value) where keys are sorted.
That datastruct will allow this exploration of
middle ground between iotavm and binufnode.
*/
public class SlowVM{
	private SlowVM(){}
	
	public static final Map<String,Map> hashToOb = new HashMap();
	
	public static final Map<String,String> hashToName = new HashMap();
	
	public static final Map<String,String> nameToHash = new HashMap();
	
	/** hashes recursively if not cashed, and stores that cache in the "this" key of each Map */
	public static String hash(Map m){
		String thisHash = (String) m.get("this");
		if(thisHash != null) return thisHash;
		String[] keys = (String[]) m.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		StringBuilder sb = new StringBuilder();
		for(String key : keys){
			if(!key.equals("this")){
				Object value = m.get(key);
				//if(value instanceof Map) value = hash((Map)value); //exponentially slow, it appears, cuz repeats calculations
				if(value instanceof Map) value = dedup((Map)value); //fast
				if(sb.length() > 0) sb.append(' ');
				sb.append(key).append("=").append(value);
			}
		}
		return hash(sb.toString());
	}
	
	public static void putNameOb(String name, Map ob){
		ob = dedup(ob);
		String h = hash(ob);
		hashToName.put(h, name);
		nameToHash.put(name, h);
	}
	
	public static String getNameOrNullFromOb(Map ob){
		ob = dedup(ob);
		String h = hash(ob);
		return hashToName.get(h);
	}
	
	public static Map getObOrNullFromName(String name){
		String h = nameToHash.get(name);
		if(h == null) return null;
		return hashToOb.get(h);
	}
	
	/** for eclipse "detail formatter" view */
	public static String debuggerViewOfMap(Map m){
		return toString(m)+" aka\r\n"+toStringRaw(m);
	}
	
	/** no abbreving nonleafs such as s and k. */
	public static String toStringRaw(Map ob){
		if(isLeaf(ob)) {
			return getNameOrNullFromOb(ob);
		}else{
			return "{"+toStringRaw(left(ob))+" "+toStringRaw(right(ob))+"}";
		}
	}
	
	public static String toString(Map ob){
		//TODO more advanced syntax that returns "{a b c d} instead of "{{{a b} c} d}"
		//TODO optimize: use StringBuilder and a Stack instead of recursion.
		String n = getNameOrNullFromOb(ob);
		if(n != null) return n;
		if(isLeaf(ob)) throw new Error("Expected nonleaf: "+hash(ob));
		return "{"+toString(left(ob))+" "+toString(right(ob))+"}";
	}
	
	public static Map dedup(Map m){
		String hash = hash(m);
		Map firstMap = (Map) hashToOb.get(hash);
		if(firstMap != null) return firstMap;
		hashToOb.put(hash, m);
		return m;
	}
	
	public static String hash(String s){
		return Text.bytesToHex(HashUtil.sha256(Text.stringToBytes(s)));
	}
	
	public static Map leaf(String leafType){
		Map m = new HashMap();
		m.put("leafType", leafType);
		m.put("isRet", true);
		
		//FIXME left and right. Should left be {iota iota} aka identityFunc,
		//and right is self? That would have to be dynamic if merkle.
		
		return dedup(m);
	}
	
	/** Doesnt check the things that pair(Map,Map) checks. Needed to avoid infinite loop during boot. */
	static Map bootPair(Map left, Map right){
		Map m = new HashMap();
		m.put("left", left);
		m.put("right", right);
		m.put("isRet", true);
		return m;
	}
	
	public static Map pair(Map left, Map right){
		Map m = new HashMap();
		//FIXME m.put("isRet", WHAT);
		m.put("left", left);
		m.put("right", right);
		m.put("isRet", shouldBeRet(m));
		return dedup(m);
	}
	
	public static boolean isLeaf(Map m){
		return m.containsKey("leafType");
	}
	
	/** If isLeaf, left is identityFunc and right is the leaf itself,
	else whatever its "left" and "right" keys literally are,
	because the rule is the eval of pair(left,right) must equal itself.
	*/
	public static Map left(Map m){
		return isLeaf(m) ? identityFunc : (Map)m.get("left");
	}
	
	/** If isLeaf, left is identityFunc and right is the leaf itself,
	else whatever its "left" and "right" keys literally are,
	because the rule is the eval of pair(left,right) must equal itself.
	*/
	public static Map right(Map m) {
		return isLeaf(m) ? m : (Map)m.get("right");
	}
	
	public static void lg(String line){
		System.out.println("\r\n"+line);
	}
	
	public static boolean isRet(Map m){
		return m.get("isRet").equals(true);
	}
	
	/** danger of infinite loop or running out of memory,
	unlike evalStep which always halts in at most forest depth.
	*/
	public static Map evalOrCrash(Map m){
		long start = System.nanoTime();
		lg("evalOrCrash starting "+toString(m));
		while(!isRet(m)){
			m = evalStep(m);
			lg("evalOrCrash step "+toString(m));
		}
		long end = System.nanoTime();
		double duration = (end-start)*1e-9;
		lg("evalOrCrash returned. duration="+duration);
		return m;
	}
	
	/** always returns in at most bigO of forest depth,
	and can be used strategicly to return in average constant depth
	especially by using iotas to emulate conses that a stack is made of
	and call evalStep from there so evalStep doesnt have to replace the whole
	path down the stack every step (down many LAZYEVALS until find first ISRET).
	<br><br>
	TODO merge some duplicate code between shouldBeRet and evalStep.
	*/
	public static Map evalStep(Map m){
		if(isRet(m)) return m;
		//TODO optimize, maybe by merging evalStep and shouldBeRet,
		//maybe by using javastack in evalOrCrash which is an optional func.
		Map L = left(m);
		Map R = right(m);
		
		//FIXME about s and sxyz etc,
		//why am I looking for LL and LLL? Should I look in RR and RRR instead?
		
		//FIXME use "left" and "right" keys instead of left() and right() here, to avoid using identityFunc in clinit?
		
		//recurse first into left, and if thats isRet then recurse into right,
		//then pair back up to original call.
		//FIXME that might not be exactly the right thing to do? Check
		//if the system computes s k cons car cdr consistently.
		if(!isRet(L)){
			return pair(evalStep(L),R);
		}else if(!isRet(R)) {
			return pair(L,evalStep(R));
		}else{
			Map LL = left(L);
			Map LR = right(L);
			if(LL == iota && LR == iota){ //((iota iota) x)
				return R; //x
			}
			Map LLL = left(LL);			
			if(LL == k){ //((Kx)y)
				return right(L); //x
			}
			if(LLL == s){ //(((Sx)y)z)
				Map x = right(LL);
				Map y = right(L);
				Map z = R;
				return pair(pair(x,z),pair(y,z)); //((xz)(yz))
			}
			if(L == iota){ //(iota x)
				//TODO optimize: If I derived iota from s and k, wouldnt need to check for iota here.
				return pair(pair(R,s),k); //((xs)k)
			}
			if(!isRet(m)) throw new Error("This check shouldnt be needed, but until get it Isnt a return: "+hash(m));
			return m;
		}
	}
	
	/** measure from the childs etc of the Map if it should be isRet or not.
	Example: Sxyz is not ret cuz its next step is replace with ((xz)(yz))
	which is not ret either. Kx is ret, but Kxy is not ret.
	<br><br>
	TODO merge some duplicate code between shouldBeRet and evalStep.
	*/
	public static boolean shouldBeRet(Map m){
		//TODO optimize, maybe by merging evalStep and shouldBeRet,
		//maybe by using javastack in evalOrCrash which is an optional func.
		
		//FIXME use "left" and "right" keys instead of left() and right() here, to avoid using identityFunc in clinit?
		
		Map L = left(m);
		Map R = right(m);
		if(!isRet(L) || !isRet(R)){
			//FIXME what if R doesnt need to be known cuz left returns a K of something that would ignore R?
			return false;
		}
		Map LL = left(L);
		Map LR = right(L);
		if(LL == iota && LR == iota){ //((iota iota) x)
			return false; //evalStep would return x
		}
		Map LLL = left(LL);	
		if(L == iota){ //(iota x)
			if(R == iota){
				return true; //FIXME? This isnt completely consistent?? but since i={I I} is part of the core logic I dont know any other way to do it?
			}else{
				return false; //evalStep would return ((xs)k)
			}
		}
		if(LL == k){ //((Kx)y)
			return false; //evalStep would return x
		}
		if(LLL == s){ //(((Sx)y)z)
			return false; //evalStep would return ((xz)(yz))
		}
		return true;
	}
	
	static final Map iota;
	
	static final Map reflect;
	
	
	//FIXME identityFunc is being called by left by shouldBeRet by pair by clinit.
	
	static final Map identityFunc;
	
	static final Map k;
	
	static final Map s;
	
	static{
		iota = leaf("iota");
		reflect = leaf("reflect");
		identityFunc = bootPair(iota,iota);
		k = bootPair(iota,bootPair(iota,identityFunc));
		s = bootPair(iota,k);
		/*FIXME identityFunc should not be isRet. It should contain s and k.
		What about the other static finals?
		But s and k are derived from {iota iota} and iota is derived from s and k,
		so maybe evalStep and shouldBeRet need to check for {iota iota},
		and define identityFunc={iota iota} should be !isRet?
		Or maybe I should define iota in terms of s and k?
		*/
		
		//capital for leafs, lowercase for nonleafs
		putNameOb("I",iota);
		putNameOb("i",identityFunc);
		putNameOb("R",reflect);
		putNameOb("k",k);
		putNameOb("s",s);
	}
	
	/** compile. Example: "{{{i k} {{k {k i}} k}} R}" becomes that Map
	<br><br>
	TODO testcases for evalStep, including:
	PARAM:  {{{{s i} {k {k i}}} k} R}
	RETURN: whatever should be the next step. I'm not sure that its {{{i k} {{k {k i}} k}} R} like evalStep says, so write simpler testcases and work up to complex.
	*/
	public static Map c(String code){
		String[] tokens = tokenize(code);
		List<String> list = new ArrayList();
		for(String token : tokens){
			if(!token.equals("}")) list.add(token); //remove all "}" from tokens
		}
		tokens = list.toArray(new String[0]);
		lg("TOKENS: "+Arrays.asList(tokens));
		/*Stack<String> stack = new Stack();
		Map car = null, cdr = null;
		for(String token : tokens){
			if(token.equals("{")){
				FIXME
			}else{
				Map ob = getObOrNullFromName(token);
			}
		}
		//throw new Error("TODO");
		*/
		if(tokens.length == 0) throw new Error("no tokens in code: "+code);
		return c(tokens,0).node;
	}
	
	/** without the closing parens, since its all a binary forest dont need them */
	public static Map cShorter(String code){
		String[] tokens = tokenize(code);
		lg("TOKENS: "+Arrays.asList(tokens));
		if(tokens.length == 0) throw new Error("no tokens in code: "+code);
		return c(tokens,0).node;
	}
	
	/** tokens can only be { or names.
	The int in NodeAndInt returned is next int to continue parsing from.
	*/
	public static NodeAndInt c(String[] tokens, int index){
		Map ob = getObOrNullFromName(tokens[index]);
		if(ob != null) return new NodeAndInt(ob,index+1);
		//Since its not a name, its followed by 2 nodes to pair, which can each be variable size.
		NodeAndInt car = c(tokens,index+1);
		NodeAndInt cdr = c(tokens, car.i);
		return new NodeAndInt(pair(car.node,cdr.node),cdr.i);
	}
	
	/** startIndex is char index in String *
	static Map c(String code, int startIndex){
		//TODO should I tokenize first?
		//throw new Error("TODO");
	}*/
	
	static String[] tokenize(String code){
		if(!code.contains("{")) return new String[]{code}; //Example, code is "R".
		code += " "; //else can miss the last token
		List<String> list = new ArrayList();
		int tokenStart = 0;
		for(int i=0; i<code.length(); i++){
			char c = code.charAt(i);
			if(c == ' ' || c == '{' || c == '}'){
				String token = code.substring(tokenStart, i+1).trim();
				if(token.length() > 0){
					//TODO shouldnt have to split "k}" to "k" and "}" here, like this was before this code:
					//TOKENS: [{, {, {, s, k}, k}, R}]
					if(token.endsWith("}")){
						String t = token.substring(0,token.length()-1);
						if(t.length() > 0) list.add(t);
						list.add("}");
					}else{
						list.add(token);
					}
					//list.add(token);
				}
				tokenStart = i+1;
			}
		}
		return list.toArray(new String[0]);
	}
	
	public static void testIsRet(Map m, boolean isRet){
		m = dedup(m);
		boolean observed = isRet(m);
		if(isRet != observed) throw new Error("testIsRet failed, correct="+isRet+" observed="+observed+" node="+toString(m));
		lg("testIsRet OK "+isRet+" "+toString(m));
	}
	
	public static void testEvalStep(Map in, Map correctOut){
		in = dedup(in);
		Map observedOut = dedup(evalStep(in));
		correctOut = dedup(correctOut);
		String msg = "in="+toString(in)+" correctOut="+toString(correctOut)+" observedOut="+toString(observedOut);
		if(observedOut != correctOut) throw new Error("testEvalStep failed. "+msg);
		lg("testEvalStep OK "+msg);
	}
	
	public static void main(String[] args){
		
		lg("This is (TODO incomplete) a formal-verification of the logic I plan to optimize in iotavm andOr binufnode (some middle ground between them using ``curry where nsForkEdit is sometimes one of the curried params to forkEdit recursively).");
		lg("");
		
		lg("TODO derive cons car cdr s k funcs.");
		lg("TODO evalStep func will change Sxyz to ((xz)(yz)) lazyeval !isRet, and change Kxy to x, and change (iota x) to ((xs)k) and FIRST will recurse through all lazyevals (!isRet) until find first isRet, first into left recursively then into right recursively, just doing 1 tiny piece of work per evalStep then pairing back up to where it was called from.");
		
		lg("reflect="+hash(reflect));
		lg("reflect="+toString(reflect));
		
		Map otherIdentityFunc = pair(pair(s,k),k);
		lg("otherIdentityFunc="+hash(otherIdentityFunc));
		lg("otherIdentityFunc="+toString(otherIdentityFunc));
		
		boolean isRet_otherIdentityFunc = isRet(otherIdentityFunc);
		lg("isRet_otherIdentityFunc="+isRet_otherIdentityFunc);
		
		lg("isRet_reflect="+isRet(reflect));
		if(!isRet(reflect)) throw new Error("isRet_reflect is false");
		
		Map call_otherIdentityFunc_on_reflect = pair(otherIdentityFunc,reflect);
		boolean isRet_call_otherIdentityFunc_on_reflect = isRet(call_otherIdentityFunc_on_reflect);
		lg("isRet_call_otherIdentityFunc_on_reflect="+isRet_call_otherIdentityFunc_on_reflect);
		if(isRet_call_otherIdentityFunc_on_reflect) throw new Error("isRet_call_otherIdentityFunc_on_reflect is true");
		
		lg("call_otherIdentityFunc_on_reflect="+hash(call_otherIdentityFunc_on_reflect));
		lg("call_otherIdentityFunc_on_reflect="+toString(call_otherIdentityFunc_on_reflect));
		
		Map evaled_call_otherIdentityFunc_on_reflect = evalOrCrash(call_otherIdentityFunc_on_reflect);
		lg("evaled_call_otherIdentityFunc_on_reflect (should equal reflect) = "+hash(evaled_call_otherIdentityFunc_on_reflect));
		if(dedup(reflect) != dedup(evaled_call_otherIdentityFunc_on_reflect)) throw new Error("evaled_call_otherIdentityFunc_on_reflect != reflect");
		
		if(!isRet(identityFunc)) throw new Error("isRet_identityFunc is false");
		if(!isRet(reflect)) throw new Error("isRet_reflect is false");
		
		Map identityFunc_reflect = pair(identityFunc,reflect);
		if(isRet(identityFunc_reflect)) throw new Error("isRet_identityFunc_reflect is true");
		
		lg("identityFunc_reflect = "+toString(identityFunc_reflect));
		lg("identityFunc_reflect = "+toString(identityFunc_reflect));
		Map eval_identityFunc_reflect = evalOrCrash(identityFunc_reflect);
		lg("eval_identityFunc_reflect (should be reflect) = "+hash(eval_identityFunc_reflect));
		if(dedup(eval_identityFunc_reflect) != dedup(reflect)) throw new Error("eval_identityFunc_reflect != reflect");
		
		Map revcar = k;
		
		Map car_refect_k = pair(pair(revcar,reflect),k);
		Map eval_revcar_refect_k = evalOrCrash(car_refect_k);
		lg("eval_revcar_refect_k (should be reflect) = "+hash(eval_revcar_refect_k));
		if(dedup(eval_revcar_refect_k) != dedup(reflect)) throw new Error("eval_revcar_refect_k != reflect");
		
		//Map cdr = pair(pair(s,identityFunc),pair(k,pair(k,identityFunc))); //``si`k`ki
		Map revcdr = pair(k,identityFunc); //`k`skk or `k`II
		lg("cdr = "+toString(revcdr));
		
		Map cdr_k_reflect = pair(pair(revcdr,k),reflect);
		lg("cdr_k_reflect = "+toString(cdr_k_reflect));
		
		testIsRet(c("I"),true);
		testIsRet(c("i"),true); //same as {I I}
		testIsRet(c("{I I}"),true); //same as i
		
		//FIXME? testIsRet(c("{I {I I}}"),true); or should that be false? 
		testIsRet(c("k"),true);
		testIsRet(c("k"),true);
		
		testEvalStep(c("{s k}"),c("{s k}"));
		testEvalStep(c("{{s k} s}"),c("{{s k} s}"));
		
		/*TODO testcases for evalStep, including:
		PARAM:  {{{{s i} {k {k i}}} k} R}
		RETURN: whatever should be the next step. I'm not sure that its {{{i k} {{k {k i}} k}} R} like evalStep says, so write simpler testcases and work up to complex.
		*/
		testEvalStep(c("{{{s k} k} R}"),c("{{k R} {k R}}"));
		testEvalStep(c("{{k R} {k R}}"),c("R"));
		
		testEvalStep(c("R"),c("R"));
		testEvalStep(c("I"),c("I"));
		testEvalStep(c("i"),c("i"));
		testEvalStep(c("s"),c("s"));
		testEvalStep(c("k"),c("k"));
		
		//evalOrCrash {{{s i} {k R}} i} should return R
		testEvalStep(c("{{{s i} {k R}} i}"),c("{{i i} {{k R} i}}"));
		testEvalStep(c("{{i i} {{k R} i}}"),c("{i {{k R} i}}"));
		testEvalStep(c("{i {{k R} i}}"),c("{i R}"));
		testEvalStep(c("{i R}"),c("R"));
		
		if(evalOrCrash(c("{{{s i} {k R}} i}")) != c("R")) throw new Error("evalOrCrash(c(\"{{{s i} {k R}} i}\")) != c(\"R\")");
		
		
		//The problem appears to be i={I I} is defined as isRet and leaf BUT
		//i is defined as !isRet and occurs in many combos,
		//and s and k are made of these similar to everything is made of these.
		//I'm sure I'll find an efficient solution but will have to move these
		//puzzle pieces of possible designs around.
		
		lg("car is just k.");
		lg("Testing cdr");
		Map eval_revcdr_k_reflect = evalOrCrash(cdr_k_reflect);
		lg("eval_revcdr_k_reflect (should be reflect) = "+hash(eval_revcdr_k_reflect));
		if(dedup(eval_revcdr_k_reflect) != dedup(reflect)) throw new Error("eval_revcdr_k_reflect != reflect");
		
		//pair(cdr,)
		
		/*TODO make car cdr cons funcs with iota (exponentially many iotas of linear currys,
		but currys are 3 in cons and are just 2 in car and 2 in cdr).
		
		cons = XYZ((zy)x)
		car = k = XYx
		cdr = XYy
		*/
		
		
		/** http://www.madore.org/~david/programs/unlambda/ says about these 3 core lisp funcs:
		The <cons> function (creates a pair from its two arguments)
		is ^u^v^f``$f$u$v, i.e. ``s``s`ks``s`kk``s`ks``s`k`sik`kk.
		The <car> function (returns the pair's first element) is ^p`$pk, i.e. ``si`kk.
		The <cdr> function (returns the pair's second element) is ^p`$p`ki, i.e. ``si`k`ki.
		
		I'm using `k<identityFunc> for cdr,
		instead of ``si`k`ki (which I havent verified that implementation),
		and I'm using k for car instead of ``si`kk.
		So it appears unlambda is using a different model of car and cdr than I am.
		I'm using car is Lx.Ly.x and cdr is Lx.Ly.y and cons is Lx.Ly.Lz.((zx)y),
		which I might be wrong to do but I'll find out when I try to build AVL treemap using conses.
		Unlambda's webpage says ^u^v^f``$f$u$v, i.e. ``s``s`ks``s`kk``s`ks``s`k`sik`kk
		and that still looks compatible with the way I'm doing it cuz thats Lx.Ly.Lz.((zx)y),
		so TODO try that...
		*/
		
		//``s``s `ks``s`kk``s`ks``s`k`sik`kk
		Map cons = cShorter("{ { s { { s { k s { { s { k k { { s { k s { { s { k { s i k { k k");
		lg("cons = "+toString(cons)+" ???");
		
		/*WAIT, if cons car and cdr are defined these ways, then you would call
		{{{cons x} y} car} to get x and {{{cons x} y} cdr} to get y. 
		Is that right? Its what I want it to do, but I shouldnt call them car or cdr.
		Maybe thats why unlambda is using an extra k, to reverse the order?
		It would still be simpler to just use them in reversed order,
		but either way could be optimized.
		
		Map cons_k_reflect = pair(pair(cons,k),reflect);
		Map car_cons_k_reflect = pair(car,cons_k_reflect); //should evalOrCrash to k
		Map cdr_cons_k_reflect = pair(cdr,cons_k_reflect); //should evalOrCrash to reflect
		
		lg("Testing car_cons_k_reflect, should evalOrCrash to k...");
		Map eval_revcar_cons_k_reflect = evalOrCrash(car_cons_k_reflect);
		if(eval_revcar_cons_k_reflect != k) throw new Error("eval_revcar_cons_k_reflect != k");
		
		lg("Testing cdr_cons_k_reflect, should evalOrCrash to reflect...");
		Map eval_revcdr_cons_k_reflect = evalOrCrash(cdr_cons_k_reflect);
		if(eval_revcdr_cons_k_reflect != reflect) throw new Error("eval_revcdr_cons_k_reflect != k");
		*/
		
		//In revcar and revcdr, rev means reversed cuz:
		//{{{cons x} y} revcar} to get x and {{{cons x} y} revcdr} to get y.
		
		Map cons_k_reflect = pair(pair(cons,k),reflect);
		Map cons_k_reflect_revcar = pair(cons_k_reflect,revcar); //should evalOrCrash to k
		Map cons_k_reflect_revcdr = pair(cons_k_reflect,revcdr); //should evalOrCrash to reflect
		
		lg("Testing cons_k_reflect_revcar, should evalOrCrash to k...");
		Map eval_cons_k_reflect_revcar = evalOrCrash(cons_k_reflect_revcar);
		if(eval_cons_k_reflect_revcar != k) throw new Error("eval_cons_k_reflect_revcar != k");
		
		lg("Testing cons_k_reflect_revcdr, should evalOrCrash to reflect...");
		Map eval_cons_k_reflect_revcdr = evalOrCrash(cons_k_reflect_revcdr);
		if(eval_cons_k_reflect_revcdr != reflect) throw new Error("eval_cons_k_reflect_revcdr != reflect");
		
		/*TODO use extra k or something, to reverse the order so car and cdr are before the cons...
		Do I want to use {{cons x} y} as a func whose param is car or cdr? Would be simpler.
		But I at least want the car and cdr funcs just to make sure I can do it.
		
		It makes more sense for {{cons x} y} to be a func that calls its param on x
		then whatever that returns calls it on y and returns what that returns,
		a way to stream in 2 params into any given func (such as revcar aka k or revcdr).
		*/
		
		//http://www.madore.org/~david/programs/unlambda/ says car is ``si`kk
		Map car = cShorter("{ { s i { k k");
		
		//http://www.madore.org/~david/programs/unlambda/ says cdr is ``si`k`ki
		Map cdr = cShorter("{ { s i { k { k i");
		
		Map car_cons_k_reflect = pair(car,cons_k_reflect); //should evalOrCrash to k
		Map cdr_cons_k_reflect = pair(cdr,cons_k_reflect); //should evalOrCrash to reflect
		
		lg("Testing car_cons_k_reflect, should evalOrCrash to k...");
		Map eval_car_cons_k_reflect = evalOrCrash(car_cons_k_reflect);
		if(eval_car_cons_k_reflect != k) throw new Error("eval_car_cons_k_reflect != k");
		
		lg("Testing cdr_cons_k_reflect, should evalOrCrash to reflect...");
		Map eval_cdr_cons_k_reflect = evalOrCrash(cdr_cons_k_reflect);
		if(eval_cdr_cons_k_reflect != reflect) throw new Error("eval_cdr_cons_k_reflect != reflect");
		
		//revcar and car are eachothers counterpart, depending if you want the cons before or after.
		//revcdr and cdr are eachothers counterpart, depending if you want the cons before or after.
		//It works.
		
		//This software is derived from https://en.wikipedia.org/wiki/SKI_combinator_calculus
		//but not from those few statements of fact on the unlambda webpage.
		//This software differs from unlambda in being completely stateless and optimizations.
		
		
		lg("TODO After get cons car cdr and AVL treemap working in iota, consider simplifying it maybe with s0<> s1<x> s2<x,y> k0<> k1<x> etc, without the need for iota whose only purpose is to get the s and k out of. Since there are other leafs, such as R is reflect to get {{cons itsCar} itsCdr}, and such as dedup and leafEquals, its not too far of a stretch to have those 5 s and k controlflow operators.");
		
		lg("TODO Have to get SlowVM working before optimizations,"
			+" including Blob (powOf2 size binheap indexed bitstring, and acyclicFlow music optimization,"
			+" and opencl optimization of matrix multiply etc.");
		
		lg("TODO find some middle ground between iota and binufnode that does ``curry efficiently using a few extra leaf types, then derive AVL treemap and use it to emulate nsForkEdit as just another curry in some funcs passed recursively and forkEdited.");
		
		lg("TODO find some middle ground between iota and binufnode that does ``curry efficiently using a few extra leaf types, then derive AVL treemap and use it to emulate nsForkEdit as just another curry in some funcs passed recursively and forkEdited.");
		
		lg("TODO Use iotavm/binufnode for the practical purpose of pre and post processing mouse movements for mouseai feature vectors.");
		
		lg("TODO acyclicFlow music optimization, and build some music funcs hooking into jsoundcard.");
		
		lg("TODO opencl optimizations to port learnloop/RBM into iotavm/binufnode, keeping in mind it only supports int long etc, not float or double, cuz float and double often have nondeterministic roundoff especially in opencl.");
	}

}



