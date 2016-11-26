$(document).ready(function() {

	document.getElementById( 'input-box' ).scrollIntoView();
	
	var messages = $("#messages");

	$(function poll() {
		setTimeout(function() {
			$.ajax({
				url : "/sm/messages",
				dataType: "json",
				success : function(data){
					var currentNumber = $(".message").length;
					for (var i = currentNumber; i < data.length; i++) {
						messages.append(
							'<div class="message">'
							+ '<span class="message-type">' + data[i].type + ': </span>'
							+ '<span class="message-text">'+ data[i].text +'</span>')
							+ '</div>';
				    }
					document.getElementById( 'input-box' ).scrollIntoView();
					poll();
				}}); 
			}, 500);
		});

});