#lang racket

(provide (all-defined-out)) ;; so we can put tests in a second file

; 1
(define (sequence low high stride)
  (cond
    [(< high low) '()]
    [(= high low) (cons high '())]
    [(< high (+ low stride)) (cons low '())]
    [#t (cons low (sequence (+ low stride) high stride))]
    ))

; 2
(define (string-append-map stringlist suffix)
  (map (lambda (i) (string-append i suffix)) stringlist))

; 3
(define (list-nth-mod numberlist n)
  (cond
    [(negative? n) (error "list-nth-mod: negative number")]
    [(null? numberlist) (error "list-nth-mod: empty list")]
    [#t (car (list-tail numberlist (remainder n (length numberlist))))]
    ))

; 4
(define ones (lambda () (cons 1 ones)))
(define (stream-for-n-steps s n)
  (if (zero? n)
      '()
      (cons (car (s)) (stream-for-n-steps (cdr (s)) (- n 1)))
      ))

(print (stream-for-n-steps ones 5))

; 5
(define stream-sequence
  (letrec ([f (lambda (x) (cons x (lambda () (f (+ x 1)))))])
    (lambda () (f 1))))
(print (stream-for-n-steps stream-sequence 5))
(define (funny-number-strem s n)
  (cond
    [(zero? n) '()]
    [(= (remainder (car (s)) 5) 0) (cons (* (car (s)) -1) (funny-number-strem (cdr (s)) (- n 1)))]
    [#t (cons (car (s)) (funny-number-strem (cdr (s)) (- n 1)))]
  ))
(print (funny-number-strem stream-sequence 23))

; 6
(define (dan-or-dog name)
  (if (equal? name "dog.jpg")
      "dan.jpg"
      "dog.jpg"))
(define dan-then-dog
  (letrec ([f (lambda (x) (cons x (lambda () (f (dan-or-dog x)))))])
  (lambda () (f "dan.jpg"))))
(print (stream-for-n-steps dan-then-dog 5))

; 7
(define (stream-add-zero s)
  (letrec ([f (lambda (x) (cons (cons 0 (car (x))) (lambda () (f (cdr (x))))))])
    (lambda () (f s))))

; 8 copy from internet, i gave up
(define (cycle-lists xs ys)
  (define (f n) (cons (cons
                       (list-nth-mod xs n)
                       (list-nth-mod ys n))
                      (lambda () (f (+ n 1)))))
  (lambda () (f 0)))

; 9
(define (vector-assoc v vec)
  (define (f n)
    (if (>= n (vector-length vec))
        #f
        (if (and
             (pair? (vector-ref vec n))
             (eq? (car (vector-ref vec n)) v))
            (vector-ref vec n)
            (f (+ n 1)))))
  (f 0))
  
(define vec (vector 6 7 (cons 1 2) (cons 3 4) (cons 4 5)))
(vector-assoc 5 vec)
; dont consider cases where we have more than one element with v

; 10
(define (cached-assoc xs n)
  (let ((cache (make-vector n #f))
        (cache-index 0))
    (lambda (k)
      (or (vector-assoc k cache)
          (let ((pair (assoc k xs)))
            (when pair
              (vector-set! cache cache-index pair)
              (set! cache-index (modulo (+ 1 cache-index) n)))
            pair
      )))))
(define xs (list (list 1 2) (list 3 4)))

(define f (cached-assoc xs 5))

(f 3)
(f 1)
(f 3)





