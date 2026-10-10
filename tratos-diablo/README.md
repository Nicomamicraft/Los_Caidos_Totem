# Tratos con el Diablo (Fabric 1.21.1)

Requisitos: JDK 21 y Fabric API.

Compilar:  gradle wrapper && ./gradlew build   (el .jar queda en build/libs/)
Probar:    ./gradlew runClient

Comandos:
/give @s tratos_diablo:trato_comun
/give @s tratos_diablo:trato_avanzado
/give @s tratos_diablo:trato_supremo
/give @s tratos_diablo:trato_legendario

Ajusta los números en DevilTier.java.
Texturas: ahora reutilizan la del tótem vanilla; para propias, crea
assets/tratos_diablo/textures/item/<nombre>.png y cambia "layer0" en models/item/<nombre>.json.
