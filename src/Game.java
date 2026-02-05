import java.io.IOException;
import java.util.*;

public class Game {
    private boolean russian = true;
    private byte countMissTakes = 0;
    String[] exit = new String[]{"выход", "Выход", "ВЫХОД", "выйти", "exit", "Exit", "EXIT"};  // массив строк, триггерящих выход из программы
    String[] back = {"назад", "Назад", "НАЗАД", "back", "Back", "Back", "out"};  // массив строк, триггерящих выход из игры
    String[] beginGame = {" ", "ye", "y", "yes", "да", "д", ""};
    String[] notBeginGame = {"не", "no", "нет", "н", "n"};
    boolean flagChangeNewGame = false;
    boolean flagOfStepBack;
    boolean newGame;
    boolean win = false;
    private short counterLoses;
    private short counterVictory;
    private List<Byte> indexsOfOpenSymbols = new ArrayList<>(); // здесь хранятся номера букв, не помеченных решётками
    private boolean game = true;
    private byte stages = 6;    // 6 8 или 10 - количество максимальных ошибок. от них зависит как будет рисоваться виселица
    private String letter;          // буква введённая пользователем
    private String closeWord;       // строка, где закрытые буквы уже закрыты
    private byte countOfOpenSymbols = 2; // количество изначально открытых символов в слове
    private byte[] indexOfRandom = new byte[countOfOpenSymbols];    // индексы рандомных чисел, столько индексов сколько указано выше
    private byte counterIndexsOfRandom;     // счётчик случайных чисел, он прибавляется когда в массив идёт случайное число
    private List <Byte> indexsOfUserAddLet = new ArrayList<>(); // здесь хранятся индексы добавленных букв
    private List <Character> userLet = new ArrayList<>();

    // функция, куда посылаешь коллекцию с индексами, число символов, само слово и символ на место закрытой буквы, и она возвращает строку, которую и печатаем
    private String closeWord(List<Byte> arrayIndexs, int countSymbols, char[] word, char symbol){
        String closeWord = "";
        char letter; // буква, которую мы записываем в слово
        for (byte i=0; i<(countSymbols); i++) {
            if (arrayIndexs.contains(i)) { // если i(номер повтора) равен любому числу из коллекции индексов, то в букву пишем символ из массива
                letter = word[i];
            }
            else
                letter = symbol; // если нет, то пишем символ, который посылают на вход функции, будь то # или _.
            closeWord = closeWord + "[" + letter + "]";
        }
        return closeWord;
    }

    // функция, которая добавляет в коллекцию array (или массив) новое число и проверяет не равно ли оно предыдущему
    private boolean addToArray(List<Byte> array, byte addbyte){ // если в массиве array нет числа addbyte то записываем addbyte
        if (!array.contains(addbyte)) {
            array.add(addbyte); //
            return true; //возвращаем результат добавления, добавилось или нет
        }
        return false;
    }

    private void addRandom(List<Byte> array, int countSymbols){
        Random random = new Random();
        byte randomIndex = (byte) (random.nextInt(countSymbols-1)); // рандом от нуля до 5, если символов 6
        indexOfRandom[counterIndexsOfRandom] = randomIndex;
        if(!addToArray(array, randomIndex)) {
            addRandom(array, countSymbols);
            return;     // досрочно выйти из функции
        }
        counterIndexsOfRandom++;
    }

    // чтение того что ввёл пользователь
    private boolean readLetter(List<Byte> array, char[] word){


        Scanner scanner = new Scanner(System.in);
        letter = scanner.nextLine();
        conditionCheck(letter);     // проверка, не написал ли человек "назад" или "выход"
        if (!game)
            return false;       // вот такой вот костыль, если при проверке юзер указал назад, то не проверяем букву
        boolean match;
        while (letter.length() != 1) {
            System.out.println("Ошибка! вы ввели больше одной буквы или не ввели букву. Введите одну букву:");
            letter = scanner.nextLine();
        }
        if (letter.matches("[а-я]") && russian || // проверка на корректность символа
                                            !russian && letter.matches("[a-z]") || letter.matches("[ё]") && russian){
            match = true;           // буква ё не находится в регексе диапазона а-я, поэтому приходится пользоваться таким костылём
        }
        else
            match = false;
        if (!match) {
            System.out.println("Ошибка! Вы ввели некорректный символ");
            return readLetter(array, word);
        }
//        scanner.close();   //хочу закрыть сканнер, но при повторном вызове после закрытия он выдаёт ошибку. со сканнером пока не разобрался
        return checkLetter (array, letter, word);
    }

    private boolean checkLetter (List <Byte> array, String letter, char[] word){

        boolean isAddOrNo = false;
        boolean isUserArrayOfIndexHas = false;
        boolean guess = false;
        boolean isUserArrayLetHas = false;
        char let = letter.charAt(0);


        if(userLet.contains(let)) {   //если эта буква есть в коллекции введённых пользователем букв, то ставим тру
            isUserArrayLetHas = true;
        }
        else{
            userLet.add(let);
            isUserArrayLetHas = false;
        }

        for (byte b=0; b<word.length; b++){ //b - индекс числа, над которым идёт проверка

// проверка есть ли введённая буква в уже отгаданных
// и вывод что буква уже открыта только если такой буквы нет в изначально открытых символах
            if (let==word[b]){ // если в загаданном слове есть эта буква
                // проверять открыта ли буква или нет
                // то есть
                // проверять есть ли b в коллекции array
                if(!array.contains(b)){ // если индекса нет в коллекции открытых индексов, ставим true. значит это новый
                                         // индекс для новой или существующей буквы и мы его точно должны вписать
                    guess = true;
                }
                if(indexsOfUserAddLet.contains(b)) {   //если этот индекс есть в коллекции пользовательских индексов, то ставим тру
                    isUserArrayOfIndexHas = true;
                }
                isAddOrNo = addToArray(array, b);
                if (isAddOrNo){
                    indexsOfUserAddLet.add(b);    //записывем индексы чисел, которые открылись, в нашу коллекцию indexsOfUsersLet
                }
            }
        }
        if (!isAddOrNo && isUserArrayOfIndexHas){
            System.out.println("Ошибка! Эту букву вы уже открыли!");
            guess = true;
        }
        // добавил проверку на все введённые буквы
        else if (!isAddOrNo && isUserArrayLetHas){
            System.out.println("Ошибка! Эту букву вы уже вводили!");
            guess = true;
        }

        if (!guess)
            countMissTakes++;
        return guess;
    }

    private void openLetters(List<Byte> indexsOfOpenSymbols, byte countOfOpenSymbols, Word word){
        for (byte b=0; b<countOfOpenSymbols; b++) //добавить в массив indexOf... столько рандомных индексов кого открыть, сколько задано в поле
            addRandom(indexsOfOpenSymbols, word.countSymbols);                              //countOfOpenSymbols

    }

    private void conditionCheck(String letter){ // проверяет не набрал ли пользователь назад или выход

        for (String s : back){
            if (Objects.equals(letter, s)){
//                System.out.println("Назад"); // раскомментировать в случае необходимости объявления действия
                game=false;
                flagOfStepBack=true;
            }
        }

        for (String s : exit){
            if (Objects.equals(letter, s)){
//                System.out.println("Выход"); // раскомментировать в случае необходимости объявления действия
                System.exit(0);
            }
        }
    }

    public void startNewGame() throws IOException {

        System.out.println("Начать новую игру?");
        /*прочитать да или нет
        если нет, выходим из программы
        если да или ентер, то game = true; и обнуляем всё, связанное со словом:
        число ошибок, indexsofopens
        если что-то кроме да и нет(проверяется флагом), то запускаем эту функцию заново
         */

        Scanner scanner_isNewGame = new Scanner(System.in);    // работа со входом терминала, считываем символ энтер или нет
        String isNewGame = scanner_isNewGame.nextLine();

        newGame = checkNewGame (isNewGame) ;


        if (!flagChangeNewGame){
            return; // если в строке что-то кроме да и нет, возвращаем ретурн, то есть отменяем выполнение этой функции.
        }           // в вайл тру он запустит функцию по-новой и по новой будет ждать строчку от пользователя

//        scanner_isNewGame.close(); // я хочу его закрыть но он выдаёт ошибку! и никакой сканер потом уже не откроешь
        // я даже пробовал с try-with-resources, оно всё равно выдаёт ошибку!!!
        Word word = new Word(); // генерируем слово
        while (word.countSymbols<=countOfOpenSymbols){
            word = new Word();  // проверяем, не меньше ли размер слова количества изначально открытых букв
        }                       // у нас же не может быть 7 открытых символов в слове из 6 букв
        playNewGame(word, newGame);


    }

    // тоже самое, но с указанием пути до файла
    public void startNewGame(String path) throws IOException {

        System.out.println("Начать новую игру?");
        /*прочитать да или нет
        если нет, выходим из программы
        если да или ентер, game = true; и обнуляем всё, связанное со словом:
        число ошибок, indexsofopens
         */
        boolean newGame; // ниже будет работа со входом терминала, считываем символ энтер или нет
        Scanner scanner_isNewGame = new Scanner(System.in);
        String isNewGame = scanner_isNewGame.nextLine();
        newGame = checkNewGame (isNewGame);

        if (!flagChangeNewGame){
            return; // если в строке что-то кроме да и нет, возвращаем ретурн, то есть отменяем выполнение этой функции.
        }           // в вайл тру он запустит функцию по-новой и по новой будет ждать строчку от пользователя
//        scanner_isNewGame.close(); // я хочу его закрыть но он выдаёт ошибку! и никакой сканер потом уже не откроешь
        Word word = new Word(path); // генерируем слово
        while (word.countSymbols<=countOfOpenSymbols){
            word = new Word(path);      // проверяем, не меньше ли размер слова количества изначально открытых букв
        }                               // у нас же не может быть 7 открытых символов в слове из шести букв
        playNewGame(word, newGame);

    }

    private void playNewGame(Word word, boolean newGame){

        if (newGame){
            clearWord(word);
        }
        if (!newGame && flagChangeNewGame) {
            System.exit(0);
        }
        Gallow gallow = new Gallow();
        while (game)
            playGame(gallow, word);
        if (!game && !flagOfStepBack) {
            System.out.print("Игра закончена. ");
            if(!win){
                System.out.println("Загаданное слово было: " + word.our_word);
                counterLoses++;
            }
            System.out.println("Побед: " + counterVictory + ", Поражений: " + counterLoses + ".");
        }
        if(!game && flagOfStepBack){        // здесь прописать действия, которые будут выполняться при шаге назад, допустим такое:
            System.out.println("Вы вышли. Загаданное слово было: " + word.our_word);
        }

    }

    private boolean checkNewGame (String isNewGame) throws IOException { // Я не шарю в исключениях, я не понимаю как их правильно
        boolean newGame = false;                // обрабатывать, меня компилятор заставляет писать throw IOException. Сам я вообще не понимаю
                                                     // что это и зачем это нужно
        flagChangeNewGame = false;
        for (String s : beginGame){
            if (Objects.equals(s, isNewGame)){
                newGame = true;
                flagChangeNewGame = true;
            }
        }
        for (String s : notBeginGame){
            if (Objects.equals(s, isNewGame)){
                newGame = false;
                flagChangeNewGame = true;
            }
        }
        for (String s : exit){
            if (Objects.equals(s, isNewGame)){
                newGame = false;
                flagChangeNewGame = true;
            }
        }

        if (!flagChangeNewGame){

            System.out.println("Вы не ответили на вопрос");
            
        }
        return newGame;
    }

    private void clearWord(Word word){
        game = true;              // очистка параметров и массивов, открытие букв
        indexsOfOpenSymbols.clear();
        indexsOfUserAddLet.clear();
        userLet.clear();
        countMissTakes = 0;
        counterIndexsOfRandom = 0;
        flagOfStepBack = false;
        openLetters(indexsOfOpenSymbols, countOfOpenSymbols, word);
    }

    public void setRussianLang(boolean russian) {
        this.russian = russian;
    }

    public void setCountOfOpenSymbols(byte countOfOpenSymbols) {
        this.countOfOpenSymbols = countOfOpenSymbols;
    }

    public void setExit(String[] exit) {
        this.exit = exit;
    }

    public void setBack(String[] back) {    // слова, по которым возвращаешься в главное меню
        this.back = back;
    }

    public void setNotBeginGame(String[] notBeginGame) {    // слова, по которым отменяешь игру, например, Non
        this.notBeginGame = notBeginGame;
    }

    public void setBeginGame(String[] beginGame) {
        this.beginGame = beginGame;
    }



    private void playGame(Gallow gallow, Word word){
        // внизу метод, на вход идёт массив indexsOfOpenSymbols, на выход идёт строка с открытыми и закрытыми буквами
        closeWord = closeWord(indexsOfOpenSymbols, word.countSymbols, word.arrayOfSymbolsWord, '_');
        gallow.gallow(countMissTakes, stages);
        System.out.println("Ошибок: " + countMissTakes + "  Осталось ошибок: " + (stages-countMissTakes));
        System.out.println(closeWord);
        readLetter(indexsOfOpenSymbols, word.arrayOfSymbolsWord);
        if ((stages-countMissTakes)==0) {
            game = false;
            gallow.gallow(countMissTakes, stages);
            System.out.print("Вас повесили. ");
        }
        else if(word.countSymbols == (countOfOpenSymbols+indexsOfUserAddLet.size())){
            System.out.print("Вы победили. ");
            game = false;
            win = true;
            counterVictory++;
        }
    }



    public Game() {
        while (true) {
            try {
                startNewGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Game(String length) { // на всякий случай пусть будет такой конструктор, где можно задать длину партии, short, middle или long
        switch (length){
            case "short":
                this.stages = 6;
                break;
            case "middle":
                this.stages = 8;
                break;
            case "standard":
                this.stages = 8;
                break;
            case "long":
                this.stages = 10;
                break;
        }
        try {
            while (true) {
                startNewGame();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public Game(String length, String path) {
        switch (length){
            case "short", "classic":
                this.stages = 6;
                break;
            case "middle", "standard":
                this.stages = 8;
                break;
            case "long":
                this.stages = 10;
                break;
        }
        try {
            while (true) {
                startNewGame(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
