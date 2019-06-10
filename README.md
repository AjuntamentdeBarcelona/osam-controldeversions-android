# osam-controldeversions-android
[![](https://jitpack.io/v/AjuntamentdeBarcelona/osam-controldeversions-android.svg)](https://jitpack.io/#AjuntamentdeBarcelona/osam-controldeversions-android)
# README

# CA
## Com es fa servir?
- Afegeix aquesta dependència en el teu projecte:
```
compile 'com.github.AjuntamentdeBarcelona:osam-controldeversions-android:1.5.+'
```
- Afegir aquest codi al teu build.gradle
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

## Introducció

Aquest mòdul mostrarà un avís quan el servei avisi que hi ha una nova versió de l'app.

Aquesta alerta la podem mostrar amb un missatge i amb o sense botons de confirmació d'accions.

Tindrem tres diferents tipus d'alerta:

1. Alerta amb un missatge i / o un títol, sense botons que bloquejarà l'app completament.
2. Alerta amb un missatge i / o un títol, amb botar d ' "ok" que un cop fet clic redirigirà l'usuari a una url.
3. Alerta amb un missatge i / o un títol, amb botons de "ok" i "cancel". Si fem clic al botó de cancel·lar l'alerta desapareixerà, i si ho fem al de confirmar s'obrirà una url.

## Descàrrega dels mòduls
Des Osam es proporcionen mòduls per realitzar un conjunt de tasques comunes a totes les apps publicades per l'Ajuntament de Barcelona.

El mòdul de Control de Versions (IOS / Android) està disponible com a repositori a:
https://github.com/AjuntamentdeBarcelona/osam-controldeversions-android

## Implementació
Per crear el missatge d'alerta, únicament hem de cridar a la funció que descarregarà el json amb les variables ja definides i mostrarà l'alerta segons els valors rebuts:

```
AlertMessageService.showMessageDialog(Context context, String endpoint, String language, new AlertMessageService.AlertDialogOnNetworkErrorListener(){
    @Override
    public void onFailure(Exception error){}
    
    @Override
    public void onSucces(boolean b){}
    
    @Override
    public void onAlertDialogDismissed(){}
});
```

A la funció se li ha de passar el context de l'app, una cadena de text amb la url on es troba el json amb la configuració desitjada, l'idioma en què es vol rebre la resposta i un listener perquè l'aplicació principal pugui reaccionar en cas que la crida al servidor d'un error o si a part de la funcionalitat que ofereix la llibreria, es vol afegir alguna funcionalitat més pròpia de l'aplicació.

Per exemple: Es vol enviar un event si l'usuari cancel·la el AlertDialog. Llavors en el ( "onAlertDialogDismissed") es posaria l'enviament del event corresponent.

Si volem canviar el color del pop-up podem sobreescriure els colors simplement definint els següents <color> dins el fitxer colors.xml de l'app que utilitzi la llibreria:
    
<!-- Color title/desc -->
    <color name="colorVersionText">#000000</color>
<!-- Color buttons Cancel/Accept -->
    <color name="colorVersionAccent">#009688</color>
<!-- Color background -->
    <color name="colorVersionBackground">#fff</color>
    
## Minificación i ofuscació en Android
Quan s'aplica la minifiación i ofuscació del codi font, s'han d'afegir unes línies al fitxer Proguard per mantenir algunes classes necessàries per al funcionament de la llibreria.
- Gson
- -keep class sun.misc.Unsafe {*; }
- -keep class com.tempos21.versioncontrol.model.AlertMessageDto {*; }

## Format fitxer JSON
```
    {
        "version": "2.1.1",
        "comparisonMode": "0",
        "minSystemVersion": "5.0",
        "title": {
            "es": "Título de la alerta",
            "cat": "Títol de la alerta",
            "en": "Alert title"
        },
        "message": {
           "es": "Prueba en castellano.",
           "cat": "Prova en català.",
           "en": "Test in english."
       },
      "okButtonTitle": {
          "es": "Vale",
          "cat": "Val",
          "en": "Ok"
     },
     "cancelButtonTitle": {
          "es": "Cancelar",
          "cat": "Cancel·lar",
          "en": "Cancel"
     },
     "okButtonActionURL": "http://www.apple.com <http://www.apple.com/> "
   }
```

## Paràmetres
- versio
    - Obligatori
    - Especifica la versió mínima de l'aplicació que volem que es comprovi, per a totes aquelles versions menors (estrictament) a aquesta, es mostrarà l'alerta de control de versió i per a la resta no es mostrarà res.
- comparisonMode
    - Obligatori
    - Especifica la manera de comparació de la versió de l'app amb el mòdul
- minSystemVersion
    - Obligatori
    - Especifica a partir que versió del sistema es mostrarà l'alerta.
- title
    - Obligatori
    - Títol de l'alerta en el cas que s'hagi de mostrar.
- message
    - Obligatori
    - Missatge de l'alerta en cas que s'hagi de mostrar.
- okButtonTitle
    - Opcional
    - Títol del botó d'acceptar.
    - Si es rep aquest paràmetre juntament amb el paràmetre okButtonActionURL, es mostrarà en l'alerta un botó d'acceptar que obrirà el link que s'ha especificat en el paràmetre okButtonActionURL.
- okButtonActionURL
    - Opcional
    - Link que s'obrirà quan l'usuari seleccioni el botó d'acceptar. Per exemple: link de la nova versió de l'aplicació a l'App Store / Google Play.
    - Si es rep aquest paràmetre juntament amb el paràmetre okButtonTitle, es mostrarà en l'alerta un botó d'acceptar que obrirà el link que s'hagi especificat.
- cancelButtonTitle
    - Opcional
    - Títol del botó de cancel·lar.
    - Si es rep aquest paràmetre es mostrarà en l'alerta un botó de cancel·lar que permetrà a l'usuari tancar l'alerta i continuar utilitzant l'aplicació amb normalitat.
  
  
## Com funciona el mòdul de control de versions
Depenent del valor del paràmetre "comparisonMode" mostrarem l'alerta.

Aquest paràmetre compararà la versió instal·lada amb la qual rebem del json, en funció de tres valors:

  0. -> Mostra l'alerta a les apps amb un nombre de versió menor que el rebut de l'json
  1. -> Mostra l'alerta en les apps que tinguin el mateix valor que el rebut de l'json
  2. -> Mostra l'alerta en les apps amb un nombre de versió major que el rebut de l'json

A més, es comprovarà que la versió del SO sigui com a mínim la indicada en el fitxer de configuració.
  
# ES
## ¿Cómo se usa?
- Añade esta dependencia en tu proyecto:
```
compile 'com.github.AjuntamentdeBarcelona:osam-controldeversions-android:1.5.+'
```
- Añadir este código en tu build.gradle
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

## Introducción

Este módulo mostrará una alerta cuando el servicio avise que hay una nueva versión de la app.

Esta alerta la podemos mostrar con un mensaje y con o sin botones de confirmación de acciones.

Tendremos tres diferentes tipos de alerta:

1. Alerta con un mensaje y/o un título, sin botones que bloqueará la app completamente.
2. Alerta con un mensaje y/o un título, con botó de “ok” que una vez hecho click redirigirá al usuario a una url.
3. Alerta con un mensaje y/o un título, con botones de “ok” y “cancel”. Si hacemos click al botón de cancelar la alerta desaparecerá, i si lo hacemos al de confirmar se abrirá una url.

## Descarga de los módulos
Desde Osam se proporcionan módulos para realizar un conjunto de tareas comunes a todas las apps publicadas por el Ayuntamiento de Barcelona.

El módulo de Control de Versiones (IOS / Android) está disponible como repositorio en:
https://github.com/AjuntamentdeBarcelona/osam-controldeversions-android

## Implementación
Para crear el mensaje de alerta, únicamente tenemos que llamar a la función que descargará el json con las variables ya definidas y mostrará la alerta según los valores recibidos:

```
AlertMessageService.showMessageDialog(Context context, String endpoint, String language, new AlertMessageService.AlertDialogOnNetworkErrorListener(){
    @Override
    public void onFailure(Exception error){}
    
    @Override
    public void onSucces(boolean b){}
    
    @Override
    public void onAlertDialogDismissed(){}
});
```
A la función se le tiene que pasar el contexto de la app, una cadena de texto con la url donde se encuentra el json con la configuración deseada, el idioma en que se quiere recibir la respuesta y un listener para que la aplicación principal pueda reaccionar en caso de que la llamada al servidor de un error o si aparte de la funcionalidad que ofrece la librería, se quiere agregar alguna funcionalidad más propia de la aplicación.

Por ejemplo: Se quiere enviar un event si el usuario cancela el AlertDialog. Entonces en el (“onAlertDialogDismissed”) se pondría el envío del event correspondiente.

Si queremos cambiar el color del pop-up podemos sobrescribir los colores simplemente definiendo los siguientes <color> dentro del fichero colors.xml de la app que utilice la librería:
    
<!-- Color title/desc -->
    <color name="colorVersionText">#000000</color>
<!-- Color buttons Cancel/Accept -->
    <color name="colorVersionAccent">#009688</color>
<!-- Color background -->
    <color name="colorVersionBackground">#fff</color>
    
## Minificación y ofuscación en Android
Cuando se aplica la minifiación y ofuscación del código fuente, se tienen que añadir unas líneas al fichero ProGuard para mantener algunas clases necesarias para el funcionamiento de la librería.
- Gson
- -keep class sun.misc.Unsafe { *; }
- -keep class com.tempos21.versioncontrol.model.AlertMessageDto { *; }


## Formato fichero JSON

```
    {
        "version": "2.1.1",
        "comparisonMode": "0",
        "minSystemVersion": "5.0",
        "title": {
            "es": "Título de la alerta",
            "cat": "Títol de la alerta",
            "en": "Alert title"
        },
        "message": {
           "es": "Prueba en castellano.",
           "cat": "Prova en català.",
           "en": "Test in english."
       },
      "okButtonTitle": {
          "es": "Vale",
          "cat": "Val",
          "en": "Ok"
     },
     "cancelButtonTitle": {
          "es": "Cancelar",
          "cat": "Cancel·lar",
          "en": "Cancel"
     },
     "okButtonActionURL": "http://www.apple.com <http://www.apple.com/> "
   }
```

## Parámetros
- version
  - Obligatorio
  - Especifica la versión mínima de la aplicación que queremos que se compruebe, para todas aquellas versiones menores (estrictamente) a esta, se mostrará la alerta de control de versión y para el resto no se mostrará nada.
- comparisonMode
  - Obligatorio
  - Especifica el modo de comparación de la versión de la app con el módulo
- minSystemVersion
  - Obligatorio
  - Especifica a partir de que versión del sistema se mostrará la alerta.
- title
  - Obligatorio
  - Título de la alerta en el caso que se tenga que mostrar.
- message
  - Obligatorio
  - Mensaje de la alerta en caso que se tenga que mostrar.
- okButtonTitle
  - Opcional
  - Título del botón de aceptar.
  - Si se recibe este parámetro juntamente con el parámetro okButtonActionURL, se mostrará en la alerta un botón de aceptar que abrirá el link que se ha especificado en el parámetro okButtonActionURL.
- okButtonActionURL
  - Opcional
  - Link que se abrirá cuando el usuario seleccione el botón de aceptar. Por ejemplo: link de la nueva versión de la aplicación a l'App Store / Google Play.
  - Si se recibe este parámetro juntamente con el parámetro okButtonTitle, se mostrará en la alerta un botón de aceptar que abrirá el link que se haya especificado.
- cancelButtonTitle
  - Opcional
  - Título del botón de cancelar.
  - Si se recibe este parámetro se mostrará en la alerta un botón de cancelar que permitirá al usuario cerrar la alerta y continuar utilizando la aplicación con normalidad.

## Cómo funciona el módulo de control de versiones
Dependiendo del valor del parámetro “comparisonMode” mostraremos la alerta.

Este parámetro comparará la versión instalada con la que recibimos del json, en función de tres valores:

  0. --> Muestra la alerta en las apps con un número de versión menor que el recibido del json
  1. --> Muestra la alerta en les apps que tengan el mismo valor que el recibido del json
  2. --> Muestra la alerta en les apps con un número de versión mayor que el recibido del json

Además, se comprobará que la versión del SO sea como mínimo la indicada en el fichero de configuración.
