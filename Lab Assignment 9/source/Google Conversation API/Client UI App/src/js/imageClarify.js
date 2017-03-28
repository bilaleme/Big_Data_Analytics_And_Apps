/**
 * Created by Administrator on 3/28/2017.
 */

xbox = null;

$("#app").on('DOMNodeInserted',function(){

    count = 1;
    $(this).children().find('img').each(function(){
        app.models.predict(Clarifai.GENERAL_MODEL, $(this).attr('src')).then(
            function(response) {
                main = $('<div>');
                main.append($('<h1>').html("Image " + count++));
                $(response.data.outputs[0].data.concepts).each(function(){
                   child = $('<div>').css({
                       'display':'inline-block',
                       'margin-left':'40px',
                       'background-color':'#F6F6F6'
                   });
                   myname = $('<p>').html(this.name);
                   value = $('<p>').html(this.value);
                   child.append(myname);
                   child.append(value);
                   main.append(child);
                });
                main.append($('<br>'));
                main.append($('<br>'));
                $('#clarifai').append(main);
            },
            function(err) {
                // there was an error
            }
        );

    });


});
