
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;

public class Word {

    String our_word = "";// = new String(); // переменная, хранящая искомое слово
    private String randomW;              // из файла russian.txt из https://github.com/danakt/russian-words.git
    byte countSymbols;
    char[] arrayOfSymbolsWord;

    private static final String separatop = File.separator;  // достаёт знак, чем ваша система разделяет шаги в пути, / или \
    private static final String path = System.getProperty("user.dir") + separatop + "russian-words-master" + separatop + "russian.txt";
    // вычисляем путь
    // до проекта с помощью метода getProperty
    // и записываем его в строку path вместе с путём до
    // текстового файла со всеми словами
    private static final String path_to_words = System.getProperty("user.dir") + separatop + "words.txt"; // другой путь в случае, если он будет найден
    // а по умолчанию файла по этому пути нет

    private String ReadLineFromFile(String path, long lineNumber, Charset charset) throws IOException{
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), charset))) {
            String line;            // функция чтения определенной строчки из файла по пути path
            int currentLine = 0;
            while ((line = br.readLine()) != null) {                                    //
                                                 //
                if (currentLine == lineNumber) {
                    return line;
                }
                currentLine++;
            }
        }
        System.out.println("Ошибка! В файле нет строчки под этим числом или не удалось прочесть файл " + path + lineNumber);
        return null;
    }

    private long GetRandomNumberLineFromFile(String path, Charset charset){
        Random random = new Random();
        try {

            return random.nextLong(Files.lines(Paths.get(path), charset).count());
        }catch(IOException e) {
            System.out.println("Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта" + path);
            throw new RuntimeException(e);

//            return GetRandomNumberLineFromFile(path_to_words, charset);//random.nextLong(Files.lines(Paths.get(path_to_words), charset).count());
//            }catch (FileNotFoundException err) {
//                System.out.println("Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта");
//            } catch (IOException ex) {
//                System.out.println("Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта");
//
//            }
        }
//        } catch (IOException e) {
//            System.out.println("ЫЫЫ Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта" + e);
//            System.exit(1);
//            throw new RuntimeException(e);
//        }
//        return 0;
    }

    private String getRandomWord(String path, Charset charset, short low_threshold, short upper_threshold) {
        long lineNumber = GetRandomNumberLineFromFile(path, charset);
        try {
            randomW = ReadLineFromFile(path, lineNumber, charset);
        } catch (IOException e) {
            System.out.println("Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта" +
                        "\nили укажите путь до вашего файла в main.java в формате С:\\Desktop\\yourword.txt");
            throw new RuntimeException(e);
        }//далее я ставлю условие, чтобы в строчке не было лишних символов, например по-другому, и чтобы слово было от 4 до 10 символов
        if (randomW.length()<low_threshold || randomW.length()>=upper_threshold || randomW.contains(" ") || randomW.contains("-") || randomW.contains("'") || randomW.contains(".") || randomW.contains(","))
            return getRandomWord(path, charset);
        else
            return randomW;
    }

    private String getRandomWord(String path, Charset charset) {
        long lineNumber = GetRandomNumberLineFromFile(path, charset);
        try {
            randomW = ReadLineFromFile(path, lineNumber, charset);
        } catch (IOException e) {

            System.out.println("Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта" +
                    "\nили укажите путь до вашего файла в main.java в формате С:\\Desktop\\yourword.txt");
            throw new RuntimeException(e);
        }//далее я ставлю условие, чтобы в строчке не было лишних символов, например по-другому, и чтобы слово было от 4 до 10 символов
        if (randomW.length()<4 || randomW.length()>10)
            return getRandomWord(path, charset);    // в будущем можно сплитить найденные слова и проверять слово даже если в одной строчке написано
                                                    // несколько слов функцией strings[] = string.split("^а-я")
        else if (!randomW.matches("[а-яёa-z]+")) {  // проверяем буквы на соответствие а-я или a-z без всяких - , . и тд

                                        // в будущем можно сделать возможность указать язык в этом классе, но пока допускаем
                                        // и русские и английские буквы, так как в конструкторе этого класса нужно будет принимать поле russian из
                                        // класса Game, а в поле russian везде передавать. Это усложнит код и его читаемость, но не придаст
                                        // ничего особо важного в плане знаний. Пока что поле russian есть только в классе Game, чтобы поменять язык
                                        // нужно вызвать game.setRussian() и указать в скобках, будет язык программы русский (true) или не русский (Английский) (false)
            return getRandomWord(path, charset);
        }


        else
            return randomW;
    }

    //проверка, есть файл со словами words.txt в папке проекта. Если нет(а по умолчанию его нет), проверяем есть ли мой файл с гитхаба
    private String checkFile (String path, String path_to_words){
        File file_1 = new File(path_to_words);
        File file_2 = new File(path);
        if (file_1.exists())
            return path_to_words;
        else if (file_2.exists())
            return path;
        else {
            System.out.println("Ошибка. Не удалось найти файл. Пожалуйста, переместите файл words.txt в папку проекта" +
                    "\nили укажите путь до вашего файла в main.java в формате С:\\Desktop\\yourword.txt");
            System.exit(1);
            return null;
        }
    }

    private Charset checkCharset(){ // проверяем, можем ли мы прочитать файл в UTF-8 или в Windows-1251
        try {                       // если нет, то выходим из программы
            getRandomWord(checkFile(path, path_to_words), StandardCharsets.UTF_8);
            return StandardCharsets.UTF_8;
        }
        catch (UncheckedIOException e){
            try {
                getRandomWord (checkFile(path, path_to_words), Charset.forName("Windows-1251"));
                return Charset.forName("Windows-1251");

            } catch (Exception ex) {
                System.out.println("Ошибка поиска файла " + ex);
                System.exit(1);
                throw new RuntimeException(ex); // без этого не компилируется. оно ждёт ретурн, даже если я вышел из программы
            }
        }
    }



// основной конструктор
    Word() throws IOException { //в конструкторах почему-то ставят модификатор public. Лично я не хочу ставить, чтобы файлы, не находящиеся
        our_word = getRandomWord (checkFile(path, path_to_words), checkCharset()); // в моей папке не смогли вызвать мой конструктор
        countSymbols = (byte) randomW.length();             //можно после charset ввести нижний и верхний пороги числа букв
        assert our_word != null;
        arrayOfSymbolsWord = our_word.toCharArray();
    }



    Word(String path){      // конструкторы с заданием пути и названия кодировки.
        our_word = getRandomWord(path, checkCharset());
        countSymbols = (byte) randomW.length();
        arrayOfSymbolsWord = our_word.toCharArray();
    }
    Word(String path, String charsetName){
        our_word = getRandomWord(path, Charset.forName(charsetName));
        countSymbols = (byte) randomW.length();
        arrayOfSymbolsWord = our_word.toCharArray();
    }



}
