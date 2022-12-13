//$(".sidebar").css("display", "none");//
//$(".ctnt").css("margin-left", "10px");-->//
$(".sidebar").css("display", "block");
$(".ctnt").css("margin-left", "20%");
const toggler = () => {
  let sdbr = document.querySelector(".ctnt");
  if ($(".sidebar").is(":visible")) {
    sdbr.classList.add("help");

    $(".sidebar").css("display", "none");
    $(".ctnt").css("margin-left", "10px");
  } else {
    $(".sidebar").css("display", "block");
    $(".ctnt").css("margin-left", "20%");
  }
};

document.querySelector(".btncl").addEventListener("click", () => {
  const money = document.querySelector("#amount").value;
  console.log(money);

  fetch("/normal/create_order", {
    // Adding method type
    method: "POST",

    // Adding body or contents to send
    body: JSON.stringify({
      amount: money,
      info: "order",
    }),

    // Adding headers to the request
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
  })
    .then((resp) => {
      resp
        .clone()
        .json()
        .then((res) => {
          if (res.status === "created") {
            console.log(res.amount);
            var options = {
              key: "rzp_live_YPC0AaNW3mzppv", // Enter the Key ID generated from the Dashboard
              amount: res.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
              currency: "INR",
              name: "Contact Manager",
              description: "Test Transaction",
              image: "https://example.com/your_logo",
              order_id: res.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
              callback_url: "https://eneqd3r9zrjok.x.pipedream.net/",
              prefill: {
                name: "",
                email: "",
                contact: "",
              },
              notes: {
                address: "Razorpay Corporate Office",
              },
              theme: {
                color: "#3399cc",
              },
            };
            let rzp = new Razorpay(options);

            rzp.on("payment.failed", function (response) {
              alert(response.error.code);
              alert(response.error.description);
            });
            rzp.open();
          }
          console.log(res);
        });
      return resp.clone().json();
    })
    .catch((err) => {
      console.log(err);
    });
});
