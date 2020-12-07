<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang='eng' xmlns="http://www.w3.org/1999/html">

<head>
  <meta charset="UTF-8">
  <meta name="description" content="Web App based on a dog breeders database.">

  <!-- Responsive meta-tag -->
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
  integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  <link rel="stylesheet" href="<c:url value="/css/dog.css"/>">

  <!-- jQuery CDN -->
  <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha384-tsQFqpEReu7ZLhBV2VZlAu7zcOV+rXbYlF2cqB8txI/8aZajjp4Bqd+V6D5IgvKT" crossorigin="anonymous"></script>

  <!-- Bootstrap JS -->
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js" integrity="sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm" crossorigin="anonymous"></script>

  <!-- FontAwesome -->
  <script src='https://kit.fontawesome.com/a076d05399.js'></script>

  <!-- Icon -->
  <link rel="icon" href="<c:url value="/media/logo2.png"/>"  />
  <title>BREEDOG. search</title>


  <!-- Scripts -->
  <script src="${pageContext.request.contextPath}/js/scroll-top.js"></script>
  <script src="<c:url value="/js/auth.js"/>"> </script>
  <script src="${pageContext.request.contextPath}/js/menu_searchpage.js"> </script>

  <!-- CSS -->
  <link rel="stylesheet" href= "<c:url value="/css/search.css"/>">
  <link rel="stylesheet" href="<c:url value="/css/scroll-top.css"/>">


</head>

<body>


  <!-- MAIN CONTAINER -->
  <div class="container-fluid" id="main-container">

    <!-- TOPNAV -->

    <!-- ROW -->
    <div class="row" id="main-row">

      <!-- SIDEBAR -->

      <!-- MAIN CONTENT -->
      <div class="main-content col-12 col-sm-9 col-md-10">

        <!-- TOP SEARCH BOX-->
        <div class="container p-3">
          <form  id="queryform">
            <div class="input-group flex-nowrap">
              <input type="text" class="form-control" id="querybox" name="query" value='<%=request.getParameter("query")%>'  placeholder="Search Something!" pattern="[\w ]" >
                <div class="invalid-feedback">
                  Only letters and numbers allowed.
                </div>
                <div class="input-group-prepend">
                  <button class="btn btn_col" type="button" id="searchButton"><i class="fas fa-search"></i>
              </div>
              
            </div>
            <input type="hidden" id="page" name="page" value="1">
            <div class="row mr-3 ml-3 mt-3 p-3 rounded bg-white box-shadow">
              <div class="col mr-3 p-2"> Results: </div>

              <div class="col p-2">
                <a data-toggle="collapse" href="#search_button_collapse" role="button" aria-expanded="false" aria-controls="search_button_collapse">
                  Filters
                </a>
              </div>
              <div class="col">

                <select class="form-control" title="Order" id="pageorder" name="order">
                  <option value="" disabled selected>Order</option>
                  <option value="youngest">Youngest</option>
                  <option value="oldest">Oldest</option>
                  

                </select>
              </div>
            </div>


            <div class="collapse rounded m-4 p-1 bg-white box-shadow" id="search_button_collapse">
              <div class="row">
                <div class="col-sm-3">

                  <select class="form-control" title="Order" id="filterSex" name="sex">
                    <option value="" disabled selected>Sex</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                  </select>
                </div>
                <div class=" col-sm-3">
                  <select class="form-control" title="status" id="filterStatus" name="status">
                    <option value="" disabled selected>Status</option>
                    <option value="Adottabile">Adoptable</option>
                    <option value="Attivo">Active</option>
                    <option value="Pensione">Retired</option>
                  </select>
                </div>
                <div class="col-sm-3">
                  <input class="form-control" type="number" placeholder="Max Age" min="0" max="20" name="mxage">
                </div>
                <div class="col-sm-3">
                  <div class="input-group-prepend">
                    <button class="btn btn_col" type="button" id="filterButton" >Filter</button>
                  </div>
                </div>

              </div>
            </div>

          </form>
        </div> <!-- end of search bar container-->

        <div class="container" id="articles">
        </div>

        <ul class="pagination justify-content-center" >
          <li class="page-item page_request" id="Previous" ><a class="page-link">Previous</a></li>
          <li class="page-item page_request" id="Next" ><a class="page-link">Next</a></li>
        </ul>
      </div>

    </div>
  </div>


  <!-- SCRIPTS -->
<script type="text/javascript" src="<c:url value="/js/search.js"/>" > </script>


  <a type="button" id="back2Top" class="btn btn-info my-3 align-items-center" title="Back to top" href="#"><i class="fas  fa-chevron-up fa-lg"></i>
    <p class="btn-text text-center m-0">TOP</p>
  </a>
</body>

</html>
