(* if you use this function to compare two strings (returns true if the same
   string), then you avoid several of the functions in problem 1 having
   polymorphic types that may be confusing *)
fun same_string(s1 : string, s2 : string) =
  s1 = s2

(* put your solutions for problem 1 here *)
(* a *)
fun all_except_option_helper(xs, str) =
  case xs of
      [] => []
    | x::xs' => if same_string(x,str)
                then all_except_option_helper(xs',str)
                else x::all_except_option_helper(xs',str)

fun all_except_option(xs,str) =
  let val x = all_except_option_helper(xs,str)
  in
      if x = xs orelse x=[]
      then NONE
      else SOME x
  end



val t1 = all_except_option([],"Lia")
val t2 = all_except_option(["Lia","Renan"],"Lia")




(* b *)
fun get_substitutions1(xs,str) =
  case xs of
      [] => []
    | x::xs' => case all_except_option(x,str) of
                    NONE => get_substitutions1(xs',str)
                  | SOME eql2 => eql2 @ get_substitutions1(xs',str);

val t = get_substitutions1([["Fred","Fredrick"],["Elizabeth","Betty"],["Freddie","Fred","F"]],"Fred");


(*c*)
fun get_substitutions2(xs,str) =
  let fun f(xs,str,acc) =
        case xs of
            [] => acc
          | x::xs' => case all_except_option(x,str) of
                          NONE => f(xs',str,acc)
                        | SOME eql2 => f(xs',str,acc @ eql2)
  in f(xs,str,[])
  end


val teste = get_substitutions2([["Fred","Fredrick"],["Elizabeth","Betty"],["Freddie","Fred","F"]],"Fred");

(*d*)
fun data(xs,m,l) =
  case xs of
      []=>[]
    | x::xs' => {first = x, middle = m,last = l}::data(xs',m,l)

val x = data(["Fred","Lia"],"W","bla")


fun similar_names_aux(xs,r:{first:string,middle:string,last:string}) =
  case xs of
      [] => []
    | x::xs' => case all_except_option(x,#first r) of
                    NONE => similar_names_aux(xs',r)
                  | SOME e => data(e,#middle r,#last r) @ similar_names_aux(xs',r);

fun similar_names(xs,r) =
  case similar_names_aux(xs,r) of
      [] => []
    | ys => r::ys


val t = similar_names([["Fred","Fredrick"],["Elizabeth","Betty"],["Freddie","Fred","F"]],{first="Fred",middle="W",last="X"});


(* you may assume that Num is always used with values 2, 3, ..., 10
   though it will not really come up *)
datatype suit = Clubs | Diamonds | Hearts | Spades
datatype rank = Jack | Queen | King | Ace | Num of int
type card = suit * rank

datatype color = Red | Black
datatype move = Discard of card | Draw

exception IllegalMove

(* put your solutions for problem 2 here *)

(* a *)
fun card_color(card: card) =
  case card of
      (Spades, _) => Black
    | (Clubs, _) => Black
    | _ => Red

val it1 = card_color(Spades, King)


(*b*)
fun card_value(card: card) =
  case card of
      (_, Ace) => 11
    | (_, Num x) => x
    | _ => 10

val it2 = card_value(Spades, Ace)

(*c*)

exception Bla
fun remove_card(cs,x,e) =
  case cs of
      [] => raise e
    | c::cs'  => case c = x of
                     true => cs'
                   | _ => c::remove_card(cs',x,e)

val it3 = remove_card([(Hearts,Ace),(Clubs, King),(Hearts,Ace)], (Hearts,Ace), Bla)

(*d*)
fun all_same_color(cs) =
  case cs of
      [] => true
    | _::[] => true
    | c1::c2::cs' => case card_color(c1) = card_color(c2) of
                         true => all_same_color(c2::cs')
                       | _ => false


val it4 = all_same_color([(Clubs,King),(Clubs,Num 4),(Spades,King),(Clubs,Queen),(Diamonds,Queen)])
val it5 = all_same_color([(Clubs,King),(Clubs,King)])
val it6 = all_same_color([(Clubs,King),(Clubs,Num 4),(Spades,King),(Hearts,Queen),(Diamonds,Queen)])


(* e *)
fun sum_cards(cs) =
  case cs of
      [] => 0
    | c::cs' => card_value(c) + sum_cards(cs')

val it7 = sum_cards([(Clubs,Ace),(Clubs,King),(Clubs,Num 5)])


(*f*)
fun score(cs,goal) =
  let val diff = sum_cards(cs) - goal
  in if diff>=0
     then 3*diff
     else abs(diff)
  end
