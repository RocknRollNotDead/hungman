public class Gallow {
    // сделать функцию, на вход подаешь количество ошибок, оно рисует виселицу
    private String[] gallow = new String[7];


    void gallow(byte counter_of_misstakes, byte max_misstakes){ // на вход подаётся количество существующих ошибок и максимальное число ошибок,
                                                                // 6, 8 или 10

        if (max_misstakes==6 || max_misstakes==8){
                                                            // я не знаю как по-другому вывести эту виселицу, кроме как таким образом
            gallow[0] = "_____________";
            gallow[1] = "|         |";
            gallow[2] = "|         o";
            gallow[3] = "|";
            gallow[4] = "|";
            gallow[5] = "|";
            gallow[6] = "|";

            if (counter_of_misstakes >= 1){
                gallow[2] = "|         0";
            }
            if (counter_of_misstakes == 2){
                gallow[3] = "|         |";
            }
            if (counter_of_misstakes >= 3){
                gallow[3] = "|        /|";
            }
            if (counter_of_misstakes >= 4){
                gallow[3] = "|        /|\\";
            }
            if (counter_of_misstakes == 5){
                gallow[4] = "|        / ";
            }
            if (counter_of_misstakes >= 6){
                gallow[4] = "|        / \\";
            }
            if (counter_of_misstakes >= 7){
                gallow[5] = "|        '";
            }
            if (counter_of_misstakes == 8){
                gallow[5] = "|        ' '";
            }

        }
        if (max_misstakes==10){
            gallow[0] = "";
            gallow[1] = "|";
            gallow[2] = "|";
            gallow[3] = "|";
            gallow[4] = "|";
            gallow[5] = "|";
            gallow[6] = "|";
            if (counter_of_misstakes >= 1){
                gallow[0] = "_____________";
            }
            if (counter_of_misstakes >= 2){
                gallow[1] = "|         |";
                gallow[2] = "|         o";
            }
            if (counter_of_misstakes >= 3){
                gallow[2] = "|         0";
            }
            if (counter_of_misstakes == 4){
                gallow[3] = "|         |";
            }
            if (counter_of_misstakes >= 5){
                gallow[3] = "|        /|";
            }
            if (counter_of_misstakes >= 6){
                gallow[3] = "|        /|\\";
            }
            if (counter_of_misstakes == 7){
                gallow[4] = "|        / ";
            }
            if (counter_of_misstakes >= 8){
                gallow[4] = "|        / \\";
            }
            if (counter_of_misstakes >= 9){
                gallow[5] = "|        '";
            }
            if (counter_of_misstakes == 10){
                gallow[5] = "|        ' '";
            }
        }
        for (String i:gallow)
            System.out.println(i);

    }
}
