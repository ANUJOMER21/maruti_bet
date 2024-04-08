package com.example.betapp.misc

class GameData {
   private val spPanas = hashMapOf(
        1 to intArrayOf(128, 137, 146, 290, 236, 245, 380, 470, 489, 560, 678, 579),
        2 to intArrayOf(129, 138, 147, 156, 237, 246, 345, 390, 480, 570, 589, 679),
        3 to intArrayOf(120, 139, 148, 157, 238, 247, 256, 346, 490, 580, 670, 689),
        4 to intArrayOf(130, 149, 158, 167, 239, 248, 257, 347, 356, 590, 680, 789),
        5 to intArrayOf(140, 159, 168, 230, 249, 258, 267, 348, 357, 456, 690, 780),
        6 to intArrayOf(123, 150, 169, 178, 240, 259, 268, 349, 358, 367, 457, 790),
        7 to intArrayOf(124, 160, 278, 179, 250, 269, 340, 359, 368, 458, 467, 890),
        8 to intArrayOf(125, 134, 170, 189, 260, 279, 350, 369, 468, 378, 459, 567),
        9 to intArrayOf(126, 135, 180, 234, 270, 289, 360, 379, 450, 469, 478, 568),
        0 to intArrayOf(127, 136, 145, 190, 235, 280, 370, 389, 460, 479, 569, 578)
    )
   private val doublePanas = hashMapOf(
        0 to intArrayOf(118, 226, 244, 299, 334, 488, 550, 668, 677),
        1 to intArrayOf(100, 119, 155, 227, 335, 344, 399, 588, 669),
        2 to intArrayOf(110, 200, 228, 255, 336, 499, 660, 688, 778),
        3 to intArrayOf(166, 229, 300, 337, 355, 445, 599, 779, 788),
        4 to intArrayOf(112, 220, 266, 338, 400, 446, 455, 699, 770),
        5 to intArrayOf(113, 122, 177, 339, 366, 447, 500, 799, 889),
        6 to intArrayOf(600, 114, 277, 330, 448, 466, 556, 880, 899),
        7 to intArrayOf(115, 133, 188, 223, 377, 449, 557, 566, 700),
        8 to intArrayOf(116, 224, 233, 288, 440, 477, 558, 800, 990),
        9 to intArrayOf(117, 144, 199, 225, 388, 559, 577, 667, 900)
    )
    private  val triplePatti = arrayListOf(
       0, 111, 222, 333, 444, 555, 666, 777, 888, 999
    )
   private val families_jodi = hashMapOf(
        12 to listOf(12, 17, 21, 26, 62, 67, 71, 76),
        13 to listOf(13, 18, 31, 36, 63, 68, 81, 86),
        14 to listOf(14, 19, 41, 46, 64, 69, 91, 96),
        15 to listOf(1, 6, 10, 15, 51, 56, 60, 65),
        23 to listOf(23, 28, 32, 37, 73, 78, 82, 87),
        24 to listOf(24, 29, 42, 47, 74, 79, 92, 97),
        25 to listOf(2, 7, 20, 25, 52, 57, 70, 75),
        34 to listOf(34, 39, 43, 48, 84, 89, 93, 98),
        35 to listOf(3, 8, 30, 35, 53, 58, 80, 85),
        45 to listOf(4,      9, 40, 45, 54, 59, 90, 95)
    )
   private val cyclePatti = hashMapOf(
        "00" to listOf(100,200,300,400,500,600,700,800,900,0),
        "10" to listOf(100, 110, 120, 130, 140, 150, 160, 170, 180, 190),
        "11" to listOf(110, 111, 112, 113, 114, 115, 116, 117, 118, 119),
        "12" to listOf(112, 120, 122, 123, 124, 125, 126, 127, 128, 129),
        "13" to listOf(113, 123, 130, 133, 134, 135, 136, 137, 138, 139),
        "14" to listOf(114, 124, 134, 140, 144, 145, 146, 147, 148, 149),
        "15" to listOf(115, 125, 135, 145, 150, 155, 156, 157, 158, 159),
        "16" to listOf(116, 126, 136, 146, 156, 160, 166, 167, 168, 169),
        "17" to listOf(117, 127, 137, 147, 157, 167, 170, 177, 178, 179),
        "18" to listOf(118, 128, 138, 148, 158, 168, 178, 180, 188, 189),
        "19" to listOf(119, 129, 139, 149, 159, 169, 179, 189, 190, 199),
        "20" to listOf(120, 200, 220, 230, 240, 250, 260, 270, 280, 290),
        "22" to listOf(122, 220, 223, 224, 225, 226, 227, 228, 229, 222),
        "23" to listOf(123, 230, 233, 234, 235, 236, 237, 238, 239, 223),
        "24" to listOf(124, 240, 244, 245, 246, 247, 248, 249, 224, 234),
        "25" to listOf(125, 250, 255, 256, 257, 258, 259, 225, 235, 245),
        "26" to listOf(126, 260, 266, 267, 268, 269, 226, 236, 246, 256),
        "27" to listOf(127, 270, 277, 278, 279, 227, 237, 247, 257, 267),
        "28" to listOf(128, 280, 288, 289, 228, 238, 248, 258, 268, 278),
        "29" to listOf(129, 290, 299, 229, 239, 249, 259, 269, 279, 289),
        "30" to listOf(130, 230, 300, 330, 340, 350, 360, 370, 380, 390),
        "34" to listOf(134, 234, 334, 340, 344, 345, 346, 347, 348, 349),
        "35" to listOf(135, 350, 355, 335, 345, 235, 356, 357, 358, 359),
        "36" to listOf(136, 360, 366, 336, 346, 356, 367, 368, 369, 236),
        "37" to listOf(137, 370, 377, 337, 347, 357, 367, 378, 379, 237),
        "38" to listOf(138, 380, 388, 238, 338, 348, 358, 368, 378, 389),
        "39" to listOf(139, 390, 399, 349, 359, 369, 379, 389, 239, 339),
        "40" to listOf(140, 240, 340, 400, 440, 450, 460, 470, 480, 490),
        "44" to listOf(144, 244, 344, 440, 449, 445, 446, 447, 448, 444),
        "45" to listOf(145, 245, 345, 450, 456, 457, 458, 459, 445, 455),
        "46" to listOf(146, 460, 446, 467, 468, 469, 246, 346, 456, 466),
        "47" to listOf(147, 470, 447, 478, 479, 247, 347, 457, 467, 477),
        "48" to listOf(148, 480, 489, 248, 348, 448, 488, 458, 468, 478),
        "49" to listOf(149, 490, 499, 449, 459, 469, 479, 489, 249, 349),
        "50" to listOf(500, 550, 150, 250, 350, 450, 560, 570, 580, 590),
        "55" to listOf(155, 556, 557, 558, 559, 255, 355, 455, 555, 550),
        "56" to listOf(156, 556, 567, 568, 569, 356, 256, 456, 560, 566),
        "57" to listOf(157, 257, 357, 457, 557, 578, 579, 570, 567, 577),
        "58" to listOf(158, 558, 568, 578, 588, 589, 580, 258, 358, 458),
        "59" to listOf(159, 259, 359, 459, 559, 569, 579, 589, 590, 599),
        "60" to listOf(600, 160, 260, 360, 460, 560, 660, 670, 680, 690),
        "66" to listOf(660, 667, 668, 669, 666, 166, 266, 366, 466, 566),
        "67" to listOf(670, 167, 267, 367, 467, 567, 667, 678, 679, 677),
        "68" to listOf(680, 688, 668, 678, 168, 268, 368, 468, 568, 689),
        "69" to listOf(690, 169, 269, 369, 469, 569, 669, 679, 689, 699),
        "70" to listOf(700, 170, 270, 370, 470, 570, 670, 770, 780, 790),
        "77" to listOf(770, 177, 277, 377, 477, 577, 677, 778, 779, 777),
        "78" to listOf(178, 278, 378, 478, 578, 678, 778, 788, 789, 780),
        "79" to listOf(179, 279, 379, 479, 579, 679, 779, 789, 799, 790),
        "80" to listOf(180, 280, 380, 480, 580, 680, 780, 880, 800, 890),
        "88" to listOf(188, 288, 388, 488, 588, 688, 788, 889, 888, 880),
        "89" to listOf(189, 289, 389, 489, 589, 689, 789, 889, 890, 899),
        "90" to listOf(900, 190, 290, 390, 490, 590, 690, 790, 890, 900),
        "99" to listOf(199, 299, 399, 499, 599, 699, 799, 899, 990, 999)
    )
   private val halfRedNumbers = listOf(
       "05", "16", "27", "38", "49", "50", "61", "72", "83", "94")
   private val fullRedNumbers = listOf(
       "00", "11", "22", "33", "44", "55", "66", "77", "88", "99")
   private val combinedNumbers = halfRedNumbers + fullRedNumbers
    private val familyPannel = hashMapOf(
        "111" to listOf(111, 116, 166, 666),
        "112" to listOf(112, 117, 126, 167, 266, 667),
        "113" to listOf(113,118,145,168,465,668),
        "114" to listOf(114, 119, 146, 169, 466, 669),
        "115" to listOf(110, 115, 156, 160, 566, 660),
        "122" to listOf(122, 127, 177, 226, 267, 677),
        "123" to listOf(123, 128, 137, 178, 236, 268, 367, 678),
        "124" to listOf(124, 129, 147, 179, 246, 269, 467, 679),
        "125" to listOf(120, 125, 157, 170, 256, 260, 567, 670),
        "133" to listOf(133, 138, 188, 336, 368, 688),
        "134" to listOf(134, 139, 148, 189, 346, 369, 468, 689),
        "135" to listOf(130, 135, 158, 180, 356, 360, 568, 680),
        "144" to listOf(144, 149, 199, 446, 469, 699),
        "145" to listOf(140, 145, 159, 190, 456, 460, 569, 690),
        "155" to listOf(100, 150, 155, 556, 560, 600),
        "222" to listOf(222, 227, 277, 777),
        "223" to listOf(223, 228, 237, 278, 377, 778),
        "224" to listOf(224, 229, 247, 279, 477, 779),
        "225" to listOf(220, 225, 257, 270, 577, 770),
        "233" to listOf(233, 238, 288, 337, 378, 788),
        "234" to listOf(234, 239, 248, 289, 347, 379, 478, 789),
        "235" to listOf(230, 235, 258, 280, 357, 370, 578, 780),
        "244" to listOf(244, 249, 299, 447, 479, 799),
        "245" to listOf(240, 245, 259, 290, 457, 470, 579, 790),
        "255" to listOf(200, 250, 255, 557, 570, 700),
        "333" to listOf(333, 338, 388, 888),
        "334" to listOf(334, 339, 348, 389, 488, 889),
        "335" to listOf(330, 335, 358, 380, 588, 880),
        "344" to listOf(344, 349, 399, 448, 489, 899),
        "345" to listOf(340, 345, 359, 390, 458, 480, 589, 890),
        "355" to listOf(300, 350, 355, 558, 580, 800),
        "444" to listOf(444, 449, 499, 999),
        "445" to listOf(440, 445, 459, 490, 599, 990),
        "455" to listOf(400, 450, 455, 559, 590, 900),
        "555" to listOf(0, 500, 550, 555)
    )

    fun familyPannelList(jodi: String): List<Int> {
        val p=familyPannel.get(jodi)
        p!!.sorted()
        return p
    }
    fun familyPannelitem():ArrayList<String>{
        val list:ArrayList<String> =ArrayList()
        for((key,value) in familyPannel){
            list.add(key)
        }
        return list;
    }
    fun cyclepattiList(jodi: String): List<Int> {
        return cyclePatti.get(jodi)!!
    }

    fun cyclepattiitem():ArrayList<String>{
        val list:ArrayList<String> =ArrayList()
        for((key,value) in cyclePatti){
            list.add(key)
        }
        return list;
    }
    private fun redfamilyJodi():HashMap<String,ArrayList<String>>{
        val hashMap:HashMap<String,ArrayList<String>> =HashMap()
        hashMap["00"] = ArrayList<String>().apply {
            add("00")
            add("55")
            add("05")
            add("50")
        }
        hashMap["11"] = ArrayList<String>().apply {
                add("11")
                add("66")
                add("16")
                add("61")
            }
        hashMap["22"] = ArrayList<String>().apply {
                    add("22")
                    add("77")
                    add("27")
                    add("72")
                }
        hashMap["33"] = ArrayList<String>().apply {
                        add("33")
                        add("88")
                        add("38")
                        add("83")
                    }
        hashMap["44"] = ArrayList<String>().apply {
                            add("44")
                            add("99")
                            add("49")
                            add("94")
                            }


return hashMap
                        }
     fun redfamilykey():ArrayList<String>{
        val list:ArrayList<String> =ArrayList()
        for((key,value) in redfamilyJodi()){
            list.add(key)
        }
        return list;
    }
    fun redfamilyList(jodi:String):ArrayList<String>{
        return redfamilyJodi().get(jodi)!!
    }
    fun red_jodi(): List<String> {
        val cn:List<String> =combinedNumbers;
        cn.sorted()
        return cn;
    }
    fun family_jodi(point:Int):List<Int>{
        val jodi=families_jodi.get(point)
        jodi!!.sorted()
        return jodi
    }
    fun family_jodiList():ArrayList<Int>{
        val list:ArrayList<Int> =ArrayList()
        for((key,value) in families_jodi){
            list.add(key)
        }
        return list;
    }
    fun TriplePattiList():ArrayList<Int>{
        return triplePatti
    }
    fun SingleList():ArrayList<Int>{
        val list:ArrayList<Int> =ArrayList()
        for((key,value) in spPanas){
            list.addAll(value.toList())
        }
        return list;
    }
    fun DoubleList():ArrayList<Int>{
        val list:ArrayList<Int> =ArrayList()
        for((key,value) in doublePanas){
            list.addAll(value.toList())
        }
        return list;
    }
    fun Dp(panna: Int):List<Int>?{
        return doublePanas.get(panna)?.toList() ?: null
    }
    fun dpkeys():ArrayList<Int>{
        val list:ArrayList<Int> =ArrayList()
        for((key,value ) in doublePanas){
            list.add(key)
        }
        return list;
    }
    fun spkeys():ArrayList<Int>{
        val list:ArrayList<Int> =ArrayList()
        for((key,value ) in spPanas){
            list.add(key)
        }
        return list;
    }
    fun Sp(panna:Int): List<Int>? {
        return spPanas.get(panna)?.toList() ?: null
    }


}