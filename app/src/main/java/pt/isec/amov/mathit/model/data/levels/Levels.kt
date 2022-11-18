package pt.isec.amov.mathit.model.data.levels

enum class Levels {
    LEVEL1{
        var operations : ArrayList<String> = arrayListOf<String>("+")
        var maxNumb = 10
    },
    LEVEL2{
        var operations : ArrayList<String> = arrayListOf<String>("+")
        var maxNumb = 20
    },
    LEVEL3{
        var operations : ArrayList<String> = arrayListOf<String>("+", "-")
        var maxNumb = 20
    },
    LEVEL4{
        var operations : ArrayList<String> = arrayListOf<String>("+", "-")
        var maxNumb = 40
    },
    LEVEL5{
        var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*")
        var maxNumb = 40
    },
    LEVEL6{
        var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*")
        var maxNumb = 80
    },
    LEVEL7{
        var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*", "/")
        var maxNumb = 80
    },
    LEVEL8{
        var operations : ArrayList<String> = arrayListOf<String>("+", "-", "*", "/")
        var maxNumb = 160
    };

    fun getNextLevel(lvl : Levels?) : Levels{
        if(lvl == null){
            return LEVEL1
        }
        if(lvl == LEVEL1){
            return LEVEL2
        }
        if (lvl == LEVEL2){
            return LEVEL3
        }
        if (lvl == LEVEL3){
            return LEVEL4
        }
        if (lvl == LEVEL4){
            return LEVEL5
        }
        if (lvl == LEVEL5){
            return LEVEL6
        }
        if (lvl == LEVEL6){
            return LEVEL7
        }

        return LEVEL8
    }
}