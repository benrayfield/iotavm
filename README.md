#iotavm

(INCOMPLETE) pure functions that always halt in at most depth of datastruct forest, debugger steps, reflection, weakEquals, binary forest dedup, hookable into optimization such as intheory opencl and int as complete binary tree of leafs car and cdr

I'm still in the formal-verification steps of the software, in which I recently derived lisp's cons car and cdr similar to in unlambda, and after some more of that kind of stuff, up to deriving AVL treemap, I'll put in optimizations for int32, int64, opencl, music tools that fit in a constant memory size, etc. Here's some output from SlowVM.java, which is slow cuz of its use of java.util.Map and deduping everything and is not nearly as fast as it will be with bit masks and other optimizations...


This is (TODO incomplete) a formal-verification of the logic I plan to optimize in iotavm andOr binufnode (some middle ground between them using ``curry where nsForkEdit is sometimes one of the curried params to forkEdit recursively).



TODO derive cons car cdr s k funcs.

TODO evalStep func will change Sxyz to ((xz)(yz)) lazyeval !isRet, and change Kxy to x, and change (iota x) to ((xs)k) and FIRST will recurse through all lazyevals (!isRet) until find first isRet, first into left recursively then into right recursively, just doing 1 tiny piece of work per evalStep then pairing back up to where it was called from.

reflect=28fe53601b6e94ba7d5829b0463e24cff7288eb1829e4e77300f67a989f236c4

reflect=R

otherIdentityFunc=eba3e1cd2e2845db64de5e1cf823c2c63044b63cb5a4f7075e00c5b596c5f0f2

otherIdentityFunc={{s k} k}

isRet_otherIdentityFunc=true

isRet_reflect=true

isRet_call_otherIdentityFunc_on_reflect=false

call_otherIdentityFunc_on_reflect=5b880d0173f91df2cd466c896d06244efed322e516f165a6f361b4b45bbf73bd

call_otherIdentityFunc_on_reflect={{{s k} k} R}

evalOrCrash starting {{{s k} k} R}

evalOrCrash step {{k R} {k R}}

evalOrCrash step R

evalOrCrash returned. duration=0.021956688000000002

evaled_call_otherIdentityFunc_on_reflect (should equal reflect) = 28fe53601b6e94ba7d5829b0463e24cff7288eb1829e4e77300f67a989f236c4

identityFunc_reflect = {i R}

identityFunc_reflect = {i R}

evalOrCrash starting {i R}

evalOrCrash step R

evalOrCrash returned. duration=9.00169E-4

eval_identityFunc_reflect (should be reflect) = 28fe53601b6e94ba7d5829b0463e24cff7288eb1829e4e77300f67a989f236c4

evalOrCrash starting {{k R} k}

evalOrCrash step R

evalOrCrash returned. duration=0.0042540040000000005

eval_revcar_refect_k (should be reflect) = 28fe53601b6e94ba7d5829b0463e24cff7288eb1829e4e77300f67a989f236c4

cdr = {k i}

cdr_k_reflect = {{{k i} k} R}

TOKENS: [I]

testIsRet OK true I

TOKENS: [i]

testIsRet OK true i

TOKENS: [{, I, I]

testIsRet OK true i

TOKENS: [k]

testIsRet OK true k

TOKENS: [k]

testIsRet OK true k

TOKENS: [{, s, k]

TOKENS: [{, s, k]

testEvalStep OK in={s k} correctOut={s k} observedOut={s k}

TOKENS: [{, {, s, k, s]

TOKENS: [{, {, s, k, s]

testEvalStep OK in={{s k} s} correctOut={{s k} s} observedOut={{s k} s}

TOKENS: [{, {, {, s, k, k, R]

TOKENS: [{, {, k, R, {, k, R]

testEvalStep OK in={{{s k} k} R} correctOut={{k R} {k R}} observedOut={{k R} {k R}}

TOKENS: [{, {, k, R, {, k, R]

TOKENS: [R]

testEvalStep OK in={{k R} {k R}} correctOut=R observedOut=R

TOKENS: [R]

TOKENS: [R]

testEvalStep OK in=R correctOut=R observedOut=R

TOKENS: [I]

TOKENS: [I]

testEvalStep OK in=I correctOut=I observedOut=I

TOKENS: [i]

TOKENS: [i]

testEvalStep OK in=i correctOut=i observedOut=i

TOKENS: [s]

TOKENS: [s]

testEvalStep OK in=s correctOut=s observedOut=s

TOKENS: [k]

TOKENS: [k]

testEvalStep OK in=k correctOut=k observedOut=k

TOKENS: [{, {, {, s, i, {, k, R, i]

TOKENS: [{, {, i, i, {, {, k, R, i]

testEvalStep OK in={{{s i} {k R}} i} correctOut={{i i} {{k R} i}} observedOut={{i i} {{k R} i}}

TOKENS: [{, {, i, i, {, {, k, R, i]

TOKENS: [{, i, {, {, k, R, i]

testEvalStep OK in={{i i} {{k R} i}} correctOut={i {{k R} i}} observedOut={i {{k R} i}}

TOKENS: [{, i, {, {, k, R, i]

TOKENS: [{, i, R]

testEvalStep OK in={i {{k R} i}} correctOut={i R} observedOut={i R}

TOKENS: [{, i, R]

TOKENS: [R]

testEvalStep OK in={i R} correctOut=R observedOut=R

TOKENS: [{, {, {, s, i, {, k, R, i]

evalOrCrash starting {{{s i} {k R}} i}

evalOrCrash step {{i i} {{k R} i}}

evalOrCrash step {i {{k R} i}}

evalOrCrash step {i R}

evalOrCrash step R

evalOrCrash returned. duration=0.009942893000000001

TOKENS: [R]

car is just k.

Testing cdr

evalOrCrash starting {{{k i} k} R}

evalOrCrash step {i R}

evalOrCrash step R

evalOrCrash returned. duration=0.00315508

eval_revcdr_k_reflect (should be reflect) = 28fe53601b6e94ba7d5829b0463e24cff7288eb1829e4e77300f67a989f236c4

TOKENS: [{, {, s, {, {, s, {, k, s, {, {, s, {, k, k, {, {, s, {, k, s, {, {, s, {, k, {, s, i, k, {, k, k]

cons = {{s {{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}}} {k k}} ???

Testing cons_k_reflect_revcar, should evalOrCrash to k...

evalOrCrash starting {{{{{s {{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}}} {k k}} k} R} k}

evalOrCrash step {{{{{{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}} k} {{k k} k}} R} k}

evalOrCrash step {{{{{{k s} k} {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R} k}

evalOrCrash step {{{{s {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R} k}

evalOrCrash step {{{{s {{{k k} k} {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R} k}

evalOrCrash step {{{{s {k {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R} k}

evalOrCrash step {{{{s {k {{{k s} k} {{{s {k {s i}}} k} k}}}} {{k k} k}} R} k}

evalOrCrash step {{{{s {k {s {{{s {k {s i}}} k} k}}}} {{k k} k}} R} k}

evalOrCrash step {{{{s {k {s {{{k {s i}} k} {k k}}}}} {{k k} k}} R} k}

evalOrCrash step {{{{s {k {s {{s i} {k k}}}}} {{k k} k}} R} k}

evalOrCrash step {{{{s {k {s {{s i} {k k}}}}} k} R} k}

evalOrCrash step {{{{k {s {{s i} {k k}}}} R} {k R}} k}

evalOrCrash step {{{s {{s i} {k k}}} {k R}} k}

evalOrCrash step {{{{s i} {k k}} k} {{k R} k}}

evalOrCrash step {{{i k} {{k k} k}} {{k R} k}}

evalOrCrash step {{k {{k k} k}} {{k R} k}}

evalOrCrash step {{k k} {{k R} k}}

evalOrCrash step {{k k} R}

evalOrCrash step k

evalOrCrash returned. duration=0.5775462960000001

Testing cons_k_reflect_revcdr, should evalOrCrash to reflect...

evalOrCrash starting {{{{{s {{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}}} {k k}} k} R} {k i}}

evalOrCrash step {{{{{{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}} k} {{k k} k}} R} {k i}}

evalOrCrash step {{{{{{k s} k} {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {{{k k} k} {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {k {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {k {{{k s} k} {{{s {k {s i}}} k} k}}}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {k {s {{{s {k {s i}}} k} k}}}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {k {s {{{k {s i}} k} {k k}}}}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {k {s {{s i} {k k}}}}} {{k k} k}} R} {k i}}

evalOrCrash step {{{{s {k {s {{s i} {k k}}}}} k} R} {k i}}

evalOrCrash step {{{{k {s {{s i} {k k}}}} R} {k R}} {k i}}

evalOrCrash step {{{s {{s i} {k k}}} {k R}} {k i}}

evalOrCrash step {{{{s i} {k k}} {k i}} {{k R} {k i}}}

evalOrCrash step {{{i {k i}} {{k k} {k i}}} {{k R} {k i}}}

evalOrCrash step {{{k i} {{k k} {k i}}} {{k R} {k i}}}

evalOrCrash step {{{k i} k} {{k R} {k i}}}

evalOrCrash step {i {{k R} {k i}}}

evalOrCrash step {i R}

evalOrCrash step R

evalOrCrash returned. duration=0.514486375

TOKENS: [{, {, s, i, {, k, k]

TOKENS: [{, {, s, i, {, k, {, k, i]

Testing car_cons_k_reflect, should evalOrCrash to k...

evalOrCrash starting {{{s i} {k k}} {{{{s {{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}}} {k k}} k} R}}

evalOrCrash step {{{s i} {k k}} {{{{{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}} k} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{{{k s} k} {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {{{k k} k} {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {k {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {k {{{k s} k} {{{s {k {s i}}} k} k}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {k {s {{{s {k {s i}}} k} k}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {k {s {{{k {s i}} k} {k k}}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {k {s {{s i} {k k}}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k k}} {{{s {k {s {{s i} {k k}}}}} k} R}}

evalOrCrash step {{{s i} {k k}} {{{k {s {{s i} {k k}}}} R} {k R}}}

evalOrCrash step {{{s i} {k k}} {{s {{s i} {k k}}} {k R}}}

evalOrCrash step {{i {{s {{s i} {k k}}} {k R}}} {{k k} {{s {{s i} {k k}}} {k R}}}}

evalOrCrash step {{{s {{s i} {k k}}} {k R}} {{k k} {{s {{s i} {k k}}} {k R}}}}

evalOrCrash step {{{s {{s i} {k k}}} {k R}} k}

evalOrCrash step {{{{s i} {k k}} k} {{k R} k}}

evalOrCrash step {{{i k} {{k k} k}} {{k R} k}}

evalOrCrash step {{k {{k k} k}} {{k R} k}}

evalOrCrash step {{k k} {{k R} k}}

evalOrCrash step {{k k} R}

evalOrCrash step k

evalOrCrash returned. duration=0.41931850000000004

Testing cdr_cons_k_reflect, should evalOrCrash to reflect...

evalOrCrash starting {{{s i} {k {k i}}} {{{{s {{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}}} {k k}} k} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{{{s {k s}} {{s {k k}} {{s {k s}} {{s {k {s i}}} k}}}} k} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{{{k s} k} {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {{{s {k k}} {{s {k s}} {{s {k {s i}}} k}}} k}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {{{k k} k} {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {k {{{s {k s}} {{s {k {s i}}} k}} k}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {k {{{k s} k} {{{s {k {s i}}} k} k}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {k {s {{{s {k {s i}}} k} k}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {k {s {{{k {s i}} k} {k k}}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {k {s {{s i} {k k}}}}} {{k k} k}} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{s {k {s {{s i} {k k}}}}} k} R}}

evalOrCrash step {{{s i} {k {k i}}} {{{k {s {{s i} {k k}}}} R} {k R}}}

evalOrCrash step {{{s i} {k {k i}}} {{s {{s i} {k k}}} {k R}}}

evalOrCrash step {{i {{s {{s i} {k k}}} {k R}}} {{k {k i}} {{s {{s i} {k k}}} {k R}}}}

evalOrCrash step {{{s {{s i} {k k}}} {k R}} {{k {k i}} {{s {{s i} {k k}}} {k R}}}}

evalOrCrash step {{{s {{s i} {k k}}} {k R}} {k i}}

evalOrCrash step {{{{s i} {k k}} {k i}} {{k R} {k i}}}

evalOrCrash step {{{i {k i}} {{k k} {k i}}} {{k R} {k i}}}

evalOrCrash step {{{k i} {{k k} {k i}}} {{k R} {k i}}}

evalOrCrash step {{{k i} k} {{k R} {k i}}}

evalOrCrash step {i {{k R} {k i}}}

evalOrCrash step {i R}

evalOrCrash step R

evalOrCrash returned. duration=0.5896389510000001

TODO After get cons car cdr and AVL treemap working in iota, consider simplifying it maybe with s0<> s1<x> s2<x,y> k0<> k1<x> etc, without the need for iota whose only purpose is to get the s and k out of. Since there are other leafs, such as R is reflect to get {{cons itsCar} itsCdr}, and such as dedup and leafEquals, its not too far of a stretch to have those 5 s and k controlflow operators.

TODO Have to get SlowVM working before optimizations, including Blob (powOf2 size binheap indexed bitstring, and acyclicFlow music optimization, and opencl optimization of matrix multiply etc.

TODO find some middle ground between iota and binufnode that does ``curry efficiently using a few extra leaf types, then derive AVL treemap and use it to emulate nsForkEdit as just another curry in some funcs passed recursively and forkEdited.

TODO find some middle ground between iota and binufnode that does ``curry efficiently using a few extra leaf types, then derive AVL treemap and use it to emulate nsForkEdit as just another curry in some funcs passed recursively and forkEdited.

TODO Use iotavm/binufnode for the practical purpose of pre and post processing mouse movements for mouseai feature vectors.

TODO acyclicFlow music optimization, and build some music funcs hooking into jsoundcard.

TODO opencl optimizations to port learnloop/RBM into iotavm/binufnode, keeping in mind it only supports int long etc, not float or double, cuz float and double often have nondeterministic roundoff especially in opencl.
