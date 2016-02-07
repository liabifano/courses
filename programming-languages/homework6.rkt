;; Programming Languages, Homework 5

#lang racket
(provide (all-defined-out)) ;; so we can put tests in a second file

;; definition of structures for MUPL programs - Do NOT change
(struct var  (string) #:transparent)  ;; a variable, e.g., (var "foo")
(struct int  (num)    #:transparent)  ;; a constant number, e.g., (int 17)
(struct add  (e1 e2)  #:transparent)  ;; add two expressions
(struct ifgreater (e1 e2 e3 e4)    #:transparent) ;; if e1 > e2 then e3 else e4
(struct fun  (nameopt formal body) #:transparent) ;; a recursive(?) 1-argument function
(struct call (funexp actual)       #:transparent) ;; function call
(struct mlet (var e body) #:transparent) ;; a local binding (let var = e in body) 
(struct apair (e1 e2)     #:transparent) ;; make a new pair
(struct fst  (e)    #:transparent) ;; get first part of a pair
(struct snd  (e)    #:transparent) ;; get second part of a pair
(struct aunit ()    #:transparent) ;; unit value -- good for ending a list
(struct isaunit (e) #:transparent) ;; evaluate to 1 if e is unit else 0

;; a closure is not in "source" programs; it is what functions evaluate to
(struct closure (env fun) #:transparent) 

;; Problem 1
(define racket-list (cons 1 (cons 2 (cons 3 '()))))
(define (racket-list->mupllist racket-list)
  (cond
    [(empty? racket-list) (aunit)]
    [(integer? (car racket-list)) (apair (int (car racket-list)) (racket-list->mupllist (cdr racket-list)))]
    ))
(racket-list->mupllist racket-list)  

(define (mupllist->racket-list mupllist)
  (cond
    [(aunit? mupllist) '()]
    [(int? (apair-e1 mupllist)) (cons (int-num (apair-e1 mupllist)) (mupllist->racket-list (apair-e2 mupllist)))]
    ))
(mupllist->racket-list (racket-list->mupllist racket-list))

;; CHANGE (put your solutions here)

;; Problem 2

;; lookup a variable in an environment
;; Do NOT change this function
(define (envlookup env str)
  (cond [(null? env) (error "unbound variable during evaluation" str)]
        [(equal? (car (car env)) str) (cdr (car env))]
        [#t (envlookup (cdr env) str)]))

;; Do NOT change the two cases given to you.  
;; DO add more cases for other kinds of MUPL expressions.
;; We will test eval-under-env by calling it directly even though
;; "in real life" it would be a helper function of eval-exp.
(define (eval-under-env e env)
  (cond [(var? e) 
         (envlookup env (var-string e))]
        [(add? e) 
         (let ([v1 (eval-under-env (add-e1 e) env)]
               [v2 (eval-under-env (add-e2 e) env)])
           (if (and (int? v1)
                    (int? v2))
               (int (+ (int-num v1) 
                       (int-num v2)))
               (error "MUPL addition applied to non-number")))]
        [(int? e) e]
        [(apair? e) e]
        [(fst? e) (apair-e1 (eval-under-env (fst-e e) env))]
        [(snd? e) (apair-e2 (eval-under-env (snd-e e) env))]
        [(aunit? e) e]
        [(isaunit? e)
         (if (equal? (eval-under-env (isaunit-e e) env) (aunit))
             (int 1)
             (int 0))]
        [(ifgreater? e)
         (let ([e1 (eval-under-env (ifgreater-e1 e) env)]
               [e2 (eval-under-env (ifgreater-e2 e) env)])
           (if (> (int-num e1) (int-num e2)) (eval-under-env (ifgreater-e3 e) env)
               (eval-under-env (ifgreater-e4 e) env)))]
        [(mlet? e)
         (let ([ev (eval-under-env (mlet-e e) env)])
           (eval-under-env (mlet-body e)
                           (cons (cons (var-string (mlet-var e)) ev) env)))]
        [(fun? e) e]
        [(call? e)
         (letrec ([ffun-val   (eval-under-env (call-funexp e) env)]
                  [fformal    (fun-formal ffun-val)]
                  [fbody      (fun-body ffun-val)]
                  [cactual    (eval-under-env (call-actual e) env)]
                  [env-with-f (cons (cons (fun-nameopt ffun-val) ffun-val) env)])
           (eval-under-env
            (mlet (var fformal) ;; var
                  cactual       ;; arg
                  fbody)        ;; call
            env-with-f))]
        [#t (error "bad MUPL expression")]))

;; Do NOT change
(define (eval-exp e)
  (eval-under-env e null))

;; Problem 3
(define (ifaunit e1 e2 e3)
  (ifgreater (isaunit e1) (int 0) e2 e3))

(define (mlet* lstlst e2) "CHANGE")

(define (ifeq e1 e2 e3 e4) "CHANGE")

;; Problem 4

(define mupl-map "CHANGE")

(define mupl-mapAddN 
  (mlet "map" mupl-map
        "CHANGE (notice map is now in MUPL scope)"))

;; Challenge Problem

(struct fun-challenge (nameopt formal body freevars) #:transparent) ;; a recursive(?) 1-argument function

;; We will test this function directly, so it must do
;; as described in the assignment
(define (compute-free-vars e) "CHANGE")

;; Do NOT share code with eval-under-env because that will make
;; auto-grading and peer assessment more difficult, so
;; copy most of your interpreter here and make minor changes
(define (eval-under-env-c e env) "CHANGE")

;; Do NOT change this
(define (eval-exp-c e)
  (eval-under-env-c (compute-free-vars e) null))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; teste2
(unless (equal? (eval-exp
                 (add (fst (snd (apair (int 2) (apair (int 3) (aunit)))))
                      (mlet (var "x") (int 3) (var "x"))))
                (int 6))
  (error "taerrado"))

;; teste3
(unless (equal? (eval-exp
                 (call
                  (fun "f" "x"
                       (ifaunit (var "x")
                                (int 0)
                                (add (int 1) (call (var "f") (snd (var "x"))))))
                  (apair (int 1) (apair (int 2) (apair (int 3) (aunit))))))
                (int 3))
  (error "taerrado3"))
