# task4-hashtable

[![Build Status](https://travis-ci.com/itmo-java-basics-2020/task4-hashtable-SkeletalHunter.svg?branch=Task)](https://travis-ci.com/itmo-java-basics-2020/task4-hashtable-SkeletalHunter)


Требуется разработать реализацию ассоциативного массива - структуры данных, которая хранит пары (ключ - значение) - на основе хеш-таблицы.

Дополните класс **ru.itmo.java.HashTable**, реализовав следующие операции:

## Операция вставки/обновления

Связывает (далее *ассоциирует*) переданное значение с ключом. Если структура уже содержала данный ключ, то значение обновляется. Возвращает значение, которое было прежде ассоциировано с переданным ключом или null, если такового не было.

    Object put(Object key, Object value);

## Операция поиска

Возвращает значение, ассоциированное с переданным ключом или null, если структура его не содержит.

    Object get(Object key);

## Операция удаления

Удаляет значение по заданному ключу из структуры данных. Возвращает удаленное значение или null, если такового нет.

    Object remove(Object key);

## Операция получения размера

Возвращает количество пар ключ - значение в ассоциативном массиве

    int size();

# Детали реализации

## Потокобезопасность

Реализуемая вами структура данных **не предоставляет никаких гарантий** при конкурентном исполнении любых ее операций. В рамках этого задания вам не нужно об этом думать.

## Массив элементов

Для реализации вы должны поддерживать массив элементов, каждый из которых представляет собой пару (ключ - значение). Доступ к данному массиву извне класса должен осуществляться исключительно через его *публичные* методы, которые описаны выше и являются его публичным [API](https://ru.wikipedia.org/wiki/API). Пользователи вашего класса не должны иметь другой возможности менять или просматривать содержимое массива.

## Элементы массива

Каждый из элементов массива представляет собой пару ключ - значение. Реализуйте его в виде вложенного класса Entry, который должен содержать как минимум два поля - ключ и ассоциированное с ним значение.

## Хеш-код

Хеш-функция сопоставляет объекту целое число типа ***int***, далее будем называть результат применения хеш-функции над каким-либо объектом - **хешом** этого объекта***.*** Часто хеш-функция опирается на внутренние свойства объектов при своей работе. ******

*Хорошая хеш-функция должна обладать рядом свойств, однако для реализации данного задания это не важно ([для справки](https://ru.wikipedia.org/wiki/%D0%A5%D0%B5%D1%88-%D1%84%D1%83%D0%BD%D0%BA%D1%86%D0%B8%D1%8F))*

Важным свойством хеш-функции является то, что с ее помощью мы можем сопоставить объекту из бесконечного множества некое значение из конечного множества. Однако, из этого следует то, что у некоторых **не равных** объектов (не эквивалентных по своим свойствам), например, у двух неравных целых чисел, хеш-коды могут быть **равны**. Это важный факт для реализации хеш-таблицы.

## Добавление элемента

Для добавления значения по ключу нам необходимо найти место в массиве, куда мы будем его добавлять. Для обновления необходимо найти существующий ключ, для которого мы будем обновлять значение. Именно для этого мы и используем хеш-функцию. Вы можете получить значение хеш-функции ключа, вызвав метод класса Object - hashCode().

    int hc = key.hashCode();

Мы могли бы создать массив длиной равной Integer.MAX_VALUE и использовать хеш как индекс, куда нужно вставить новый элемент, но это может быть нецелесообразно, если ожидаемое количество хранимых пар ключ-значение сильно меньше. ***Поэтому в ваше задание входит придумать как преобразовать хеш в индекс массива.***

После того как вы преобразовали значение хеш-функции от ключа в его место в массиве вас может ждать другая проблема. Нужно вспомнить, что у разных ключей могут быть одинаковые хеши и они попадут в одинаковое место массива - это называется ***[коллизией](https://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D0%BB%D0%BB%D0%B8%D0%B7%D0%B8%D1%8F_%D1%85%D0%B5%D1%88-%D1%84%D1%83%D0%BD%D0%BA%D1%86%D0%B8%D0%B8).*** Есть разные подходы к разрешению коллизий, вам необходимо реализовать подход, основанный на открытой адресации. Справка [здесь](https://ru.wikipedia.org/wiki/%D0%A5%D0%B5%D1%88-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0#%D0%9E%D1%82%D0%BA%D1%80%D1%8B%D1%82%D0%B0%D1%8F_%D0%B0%D0%B4%D1%80%D0%B5%D1%81%D0%B0%D1%86%D0%B8%D1%8F).

Для реализации вставки элементов вам может понадобится проверять равны ли два ключа. Для этого вам необходимо использовать метод класса Object - equals()

    Object anotherKey = elements[i].getKey();
    boolean equal = key.equals(anotherKey);

## Расширение массива

Пользователи вашей структуры данных могут знать приблизительное или точное количество ключей, которые будут хранить. Они не хотели бы выделять сильно больше памяти, чем требуется для хранения этого количества пар ключ-значение. 

Вам потребуется реализовать **конструктор**, который принимает на вход целое число - начальную вместимость ассоциативного массива.

Однако, в процессе работы может выясниться, что действительное значение ключей превышает ожидаемое или операции вставки/поиска стали совершать слишком много сравнений ключей, что сказывается на производительности.

В таких случаях ваша структура данных должна расшириться. А именно, вы должны создать новый массив, в **два раза длиннее** предыдущего, выполнить все операции по переносу старых значений и заменить старый массив на новый.

При расширении вы должны подумать о 2-х свойствах (полях) своей хэш-таблицы: 

- **коэффициент нагрузки (loadFactor) -** характеризует соотношение длинны массива, в котором лежат элементы, и кол-во элементов в этом массиве. Коэффициент нагрузки является неизменяемой величиной для каждой существующей таблицы (он не изменяется после инициализации хэш-таблицы). **Значение по умолчанию** коэффициента нагрузки равно 0.5 для любой хеш-таблицы. Необходимо не только предусмотреть значение по-умолчанию, но и предусмотреть возможность передавать этот параметр в конструкторе.
- порог заполняемости (**threshold**) - значение размера хэш-таблицы (не длины массива), при котором необходимо произвести расширение. Величина threshold может меняться на протяжении существования объекта хэш-таблицы. Предусмотрите ее своевременное изменение при реализации методов для работы с хэш-таблицей.