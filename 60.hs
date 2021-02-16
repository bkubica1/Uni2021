--Group 60
--Samuel Rudd 1905414
--Bartosz Kubica 976006
--Luke Smith 978232


import Test.QuickCheck
import System.IO
import Data.List
import Data.Char

--Q1
average :: Float -> Float -> Float -> Float
average x y z = (x + y + z) / 3

--sends each number and the average value to compare'
howManyAboveAverage1 :: Float -> Float -> Float -> Int
howManyAboveAverage1 x y z = (compare' x (average x y z))
 + (compare' y (average x y z)) + (compare' z (average x y z))
--if number is above average, returns 1, otherwise returns 0
compare' :: Float -> Float -> Int
compare' x y = if (x > y) then 1 else 0
--Result:
--howManyAboveAverage1 1905414 976006 978232
--1

--turns numbers into a list, and keeps the numbers that are above the average
--then returns the length of the list to give the amount of numbers above average
howManyAboveAverage2 :: Float -> Float -> Float -> Int
howManyAboveAverage2 x y z = length (listAboveAverage (turnList3 x y z))
turnList3 :: Float -> Float -> Float -> [Float]
turnList3 x y z = [x, y, z]
--returns a list of the numbers above the average
listAboveAverage :: [Float] -> [Float]
listAboveAverage xs = [x | x <-xs, x > ((sum xs) / 3)]
--Result:
--howManyAboveAverage2 1905414 976006 978232
--1

--only keeps the numbers above the average in the list
--then returns the length of the list to give the amount of numbers above average
howManyAboveAverageL :: [Float] -> Int
howManyAboveAverageL xs = length [x | x <-xs, x > ((sum xs) / (floatLength xs))]
--needed to create this as length works with Int's and not Floats
floatLength       :: [a] -> Float
floatLength []     = 0
floatLength (_:xs) = 1 + floatLength xs
--Result:
--howManyAboveAverageL [10,14,23,3,17,20,12]
--3

--code for QuickCheck
checkCorrectness :: Float -> Float -> Float -> Bool
checkCorrectness x y z =
 (howManyAboveAverage1 x y z) == (howManyAboveAverage2 x y z)
--Result:
--quickCheck checkCorrectness
-- +++ OK, passed 100 tests.

--Q2
--these were used to test the different split functions
factors :: Int -> [Int]
factors n = [x | x <- [1..n], n `mod` x == 0]
prime1  :: Int -> Bool
prime1 n = factors n == [1,n]

--uses list comprehensions with different predicates
split1 :: (a -> Bool) -> [a] -> ([a], [a])
split1 f xs = ([x | x <-xs, f x], [x | x <-xs, ((f x) == False)])

--using filter, values that are true are put in the first part of the tuple,
--the difference between the full list and the the filtered list is used to display
--the false values in the list for the second part of the tuple
split2 :: (Eq a) => (a -> Bool) -> [a] -> ([a],[a])
split2 f xs = (filter f xs, xs \\ (filter f xs))

--uses guardF function in each tuple that passes the function, list and boolean value it wants
split3 :: (a -> Bool) -> [a] -> ([a], [a])
split3 f xs = (guardF f True xs, guardF f False xs)
--uses guards, keeps x if the predicate is true, removes it and recurses on the list otherwise
guardF :: (a -> Bool) -> Bool -> [a] -> [a]
guardF f p  [] = []
guardF f p (x:xs)
 | (f x == p)   = (x:guardF f p xs)
 | otherwise    = guardF f p xs
--Results:
--split1 prime [1905414, 976006, 978232]
--([],[1905414,976006,978232])
--split2 even [1905414, 976006, 978232]
--([1905414,976006,978232],[])
--split3 odd [1905414, 976006, 978232]
--([],[1905414,976006,978232])

--Q3
--x is diameter, top is number of toppings
alfredo :: Float -> Float -> Float
alfredo d top = round2dp((calculateArea d) * (0.001 + (0.002*top)) * 1.6)
calculateArea :: Float -> Float
calculateArea d = (((d/2)^2)*pi)
--rounds the input to 2dp to display pricing without lots of decimal places
round2dp :: Float -> Float
round2dp x = fromIntegral (round $ x * 100) / 100

--checks if the the price of the first pizza is more than the second one
pizzaCompare :: Float -> Float -> Float -> Float -> Bool
pizzaCompare d1 top1 d2 top2 = (alfredo d1 top1) > (alfredo d2 top2)
--pizzaCompare 14 5 32 2
--False
--the first pizza is 2.71, the second is 6.43 which is more expensive

--Q4
divides :: Integer -> Integer -> Bool
divides x y = y `mod` x == 0
prime :: Integer -> Bool
prime n = n > 1 && and [not(divides x n) | x <- [2..(n-1)]]
allPrimes :: [Integer]
allPrimes = [x | x <- [2..], prime x]

--finds all primes between x and y
allPrimesBetween :: Integer -> Integer -> [Integer]
allPrimesBetween x y = [x | x <- [x..y], prime x]

primeTest :: [Bool]
primeTest = [(prime x) | x <- [0..]]
--displays the number and if it is prime or not
primeTestPairs :: [(Integer, Bool)]
primeTestPairs = [(x,(prime x)) | x <- [0..]]
--Result:
--take 10 primeTestPairs
--[(0,False),(1,False),(2,True),(3,True),(4,False),(5,True),(6,False),(7,True),(8,False),(9,False)]

primeList x = take x [prime x | x <- [0..], prime x]

--creates a list of tuples containing prime twins, the length finds how many twins
primeTwins :: Int -> Int
primeTwins y = length [(x, x+2) | x <- (take y allPrimes), (prime x) && (prime (x+2))]
--Results:
--primeTwins 20
--8
--primeTwins 2000
--302

--Q5
--Results:
--phonetic ["Jones", "Winston"]
--Jones: Jonas, Johns, Saunas
--Winston:
--phonetic ["Smith", "Macdonald"]
--Smith: Smith, Smyth, Smythe, Smid, Schmidt
--Macdonald: Macdonald, Nest O'Malett

--reads the file, converts that names to filtered version and zips with names from file
getNames :: IO [(String,String)]
getNames = do
             names <- readFile "surnames.txt"
             return (zip (lines names) (stringFilter (lines names)))

--main call function. prints the name to be found along with the found names. runs recursively
phonetic :: [String] -> IO ()
phonetic [] = putStr ""
phonetic (x:xs) = do {
                  putStr (x ++ ": ") ;
                  surnames <- getNames ;
                  printStrArray (findElement (lowerFilter x) surnames) ;
                  phonetic xs
                 }

--string array as input is printed to the screen each element at a time
printStrArray :: [String] -> IO ()
printStrArray [] = putStrLn ""
printStrArray (x:[]) = putStrLn x
printStrArray (x:xs) = do {
                        putStr (x ++ ", ");
                        printStrArray xs
                       }

--finds the element that matches the filtered names from file. If matches then takes first of pair and returns
findElement :: String -> [(String,String)] -> [String]
findElement b names = [fst x | x <- names, (snd x) == b]

--creates the list of adjusted names so that they can be matched phonetically
stringFilter :: [String] -> [String]
stringFilter xs = [lowerFilter x | x <- xs]

--turns the surnames into the phonetic matching names using the other functions
lowerFilter :: [Char] -> [Char]
lowerFilter (x:xs) = remDups (letterFilter (toLower x):filterAlph [toLower x | x <- xs])

--removes the unwanted letters, and then merges the equivelant letters
filterAlph :: [Char] -> [Char]
filterAlph xs = [letterFilter x | x <- xs, x `elem` ['b','c','d','f','g','j','k','l','m','n','p','q','r','s','t','v','x','z']]

--changes the letters in the names to ones that can cover others that are equivelant
letterFilter :: Char -> Char
letterFilter x
   | x `elem` ['a','e','i','o','u']             = 'a'
   | x `elem` ['c','j','k','q','s','x','y','z'] = 'b'
   | x `elem` ['b','f','p','v','w']             = 'c'
   | x `elem` ['d','t']                         = 'd'
   | x `elem` ['m','n']                         = 'e'
   | otherwise                                  = x
 
--removes concurrent duplicate letters in the String
remDups :: Eq a => [a] -> [a]
remDups [] = []
remDups [x] = [x]
remDups (x1:x2:xs)
   | x1==x2    = remDups (x2:xs)
   | otherwise = x1:remDups (x2:xs)