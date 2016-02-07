(* append *)
fun append(x1:'a list,x2:'a list) =
  if null x1
  then x2
  else hd x1::append(tl x1,x2);

val x_t = append([1,2,3],[3,4,4])

(* 1 *)
fun is_older(d1:int*int*int,d2:int*int*int) =
  if #1 d1 >= #1 d2
  then (if #2 d1 >= #2 d2
        then (if #3 d1 >= #3 d2
              then false
              else true)
        else true)
  else true;


(* 2 *)
fun number_in_month(dates:(int*int*int) list,mes:int) =
  if null dates
  then 0
  else (if #2 (hd dates) = mes
        then 1 + number_in_month(tl dates,mes)
        else number_in_month(tl dates,mes)
       );

(* 3 *)
fun number_in_months(dates:(int*int*int) list,mes:int list) =
  if null dates orelse null mes
  then 0
  else  number_in_month(dates,hd mes) + number_in_months(dates,tl mes)

val x = number_in_months([(1,2,3),(1,2,3),(1,3,4),(1,5,4),(1,7,6)],[2,3,7]);
(* fun dates_in_months(dates:(int*int*int) list,mes:int list) = *)

(* 4 *)
fun dates_in_month(dates:(int*int*int) list,mes:int) =
  if null dates
  then []
  else (if #2 (hd dates) = mes
        then hd dates::dates_in_month(tl dates,mes)
        else dates_in_month(tl dates,mes));

(* 5 *)
fun dates_in_months(dates:(int*int*int) list,mes:int list) =
  if null dates orelse null mes
  then []
  else append(dates_in_month(dates,hd mes),dates_in_months(dates,tl mes));

(* 6 *)
fun get_nth(s:string list,n:int) =
  if null s
  then "dead"
  else (if n=1
        then hd s
        else get_nth(tl s,n-1));

(* 7 *)
fun date_to_string(date:int*int*int) =
  let val m = ["Jan","Fev","Mar","Apr","Mai","Jun","Jul","Ago","Sep","Oct","Nov","Dec"]
  in
      if  #2 date = 0
      then "Something Wrong"
      else get_nth(m,#2 date)
  end;

(* 8 *)
fun number_before_reaching_sum(sum:int,num:int list) =
  if sum - hd num <= 0
  then 0
  else 1+ number_before_reaching_sum(sum - hd num, tl num);

(* 9 *)
fun what_month(d: int) =
  let val dia = [31,29,30,31,30,31,30,31,30,31,30,31]
  in
      if d - hd dia <= 0
      then 0
      else 1+number_before_reaching_sum(d - hd dia,tl dia)
  end;

fun get_nth2(n:int,l:int list) =
  if n = 1
  then hd l
  else get_nth2(n-1,tl l);


(* 10 - de um jeito simples *)
fun month_range(day1:(int*int*int),day2:(int*int*int)) =
  let val dia = [31,29,30,31,30,31,30,31,30,31,30,31]
  in
      if (#2 day1 - #2 day2 = 0) andalso (#1 day1 - #1 day2 = 0)
      then [#2 day1]
      else(if #1 day1 - get_nth2(#2 day1,dia) = 0
           then (#2 day1)::month_range((1,(#2 day1)+1,(#3 day1)),day2)
           else (#2 day1)::month_range(((#1 day1)+1,(#2 day2),#3 day1),day2))
  end;

(* 11 *)
fun oldest(date:(int*int*int) list) =
  if null date
  then (0,0,0)
  else (
      if null (tl date)
      then hd date
      else(
          let val new = oldest(tl date)
          in
              if is_older(hd date,new)=true
              then new
              else hd date
          end));

val w = oldest([(1,1,1)])
val y = oldest([(1,2,1998),(1,3,1999),(20,1,2004)])
