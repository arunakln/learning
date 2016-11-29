using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Aruna_technicalTest.Models;
using Aruna_technicalTest.Controllers;
using System.Web.Mvc;
using System.Linq;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Unit Test cases of Home Controller class
 */

namespace Aruna_technicalTest_UnitTest
{
    
    [TestClass]
    public class TestHomeController
    {
        private IInvoiceContext testdb = new TestInvoiceContext();

        [TestMethod]
        public void getInvoiceByID_CheckNotNUll()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());

            var controller = new HomeController(testdb);
            var result = controller.getInvoiceByID(3);

            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void getInvoiceByID_ShouldReturnInvoiceWithSameID()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());

            var controller = new HomeController(testdb);
            var result = controller.getInvoiceByID(3);

            Assert.AreEqual(3, result.ID);
        }

        [TestMethod]
        public void TestIndexView_CheckCorrectSearchedCount()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());
            
            var controller = new HomeController(testdb);

            var result = controller.Index("Du") as ViewResult;
            var testQuery =(IQueryable < InvoiceModel >) result.ViewData.Model;
            Assert.AreEqual(1, testQuery.Count());
        }

        [TestMethod]
        public void TestIndexView_CheckCorrectInvoiceSearched()
        {
            AddInvoices();

             var controller = new HomeController(testdb);

            var result = controller.Index("1") as ViewResult;
            var testQuery = (IQueryable<InvoiceModel>)result.ViewData.Model;
            Assert.AreEqual("Dummy1", testQuery.First().Name);
        }

        [TestMethod]
        public void TestIndexView_CheckSearchResultCount()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());
            testdb.InvoiceList.Add(AddDummyInvoice());

            var controller = new HomeController(testdb);

            var result = controller.Index("Du") as ViewResult;
            var testQuery = (IQueryable<InvoiceModel>)result.ViewData.Model;
            Assert.AreEqual(2, testQuery.Count());
        }

        [TestMethod]
        public void TestIndexView_CheckSearchResultNotNull()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());
            testdb.InvoiceList.Add(AddDummyInvoice());

            var controller = new HomeController(testdb);

            var result = controller.Index("Du") as ViewResult;
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void CreateInvoiceView_CheckCorrectlySaved()
        {
            InvoiceModel inv = new InvoiceModel() { Name = "Testing", TaxPointDate = DateTime.Now, Address = "Dummy Address", Reference = "1234", TotalGross = 12.25 };

            var controller = new HomeController(testdb);

            var result = controller.CreateInvoice(inv) as ViewResult;
            IQueryable<InvoiceModel> testQuery = from test in testdb.InvoiceList where test.Name == "Testing" select test;
            Assert.AreEqual(testQuery.First().Name, "Testing");
        }

        [TestMethod]
        public void CreateInvoiceView_CheckCorrectlySavedCount()
        {
            InvoiceModel inv = new InvoiceModel() { Name = "Testing", TaxPointDate = DateTime.Now, Address = "Dummy Address", Reference = "1234", TotalGross = 12.25 };

            var controller = new HomeController(testdb);

            var result = controller.CreateInvoice(inv) as ViewResult;
            IQueryable<InvoiceModel> testQuery = from test in testdb.InvoiceList where test.Name == "Testing" select test;
            Assert.AreEqual(testQuery.Count(), 1);
        }

        [TestMethod]
        public void CreateInvoiceView_CheckNotNull()
        {
            InvoiceModel inv = new InvoiceModel() { Name = "Testing", TaxPointDate = DateTime.Now, Address = "Dummy Address", Reference = "1234", TotalGross = 12.25 };

            var controller = new HomeController(testdb);

            var result = controller.CreateInvoice(inv) as ViewResult;
            IQueryable<InvoiceModel> testQuery = from test in testdb.InvoiceList where test.Name == "Testing" select test;
            Assert.AreEqual(testQuery.Count(), 1);
        }

        [TestMethod]
        public void EditInvoiceView_CheckErrorMessageWhenIDNull()
        {
            var controller = new HomeController(testdb);

            var result = controller.EditInvoice(null) as ViewResult;
            Assert.IsTrue(controller.ModelState[""].Errors.Any(modelError => modelError.ErrorMessage == "Invoice ID is Null"));
        }

        [TestMethod]
        public void EditInvoiceView_CheckErrorMessageInvalidID()
        {
            var controller = new HomeController(testdb);

            var result = controller.EditInvoice(9999) as ViewResult;
            Assert.IsTrue(controller.ModelState[""].Errors.Any(modelError => modelError.ErrorMessage == "No such invoice"));
        }

        [TestMethod]
        public void EditInvoiceView_CheckresultCount()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());
            var controller = new HomeController(testdb);

            var result = controller.EditInvoice(3) as ViewResult;
            var invoice = (InvoiceModel)result.ViewData.Model;
            Assert.AreEqual(invoice.ID, 3);
        }

        [TestMethod]
        public void EditInvoiceView_CheckNotNull()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());
            var controller = new HomeController(testdb);

            var result = controller.EditInvoice(3) as ViewResult;
            var invoice = (InvoiceModel)result.ViewData.Model;
            Assert.IsNotNull(invoice);
        }

        [TestMethod]
        public void SearchInvoiceView_CheckSearchNotNUll()
        {
            AddInvoices();

            var controller = new HomeController(testdb);

            var result = controller.Index("Du") as ViewResult;
            var testQuery = (IQueryable<InvoiceModel>)result.ViewData.Model;
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void DeleteConfirmedView_CheckCorrectlyDeleted()
        {
            AddInvoices();

            var controller = new HomeController(testdb);

            var result = controller.DeleteConfirmed(1) as ViewResult;
            IQueryable<InvoiceModel> testQuery = from test in testdb.InvoiceList where test.ID == 1 select test;
            Assert.AreEqual(testQuery.Count(), 0);
        }

        [TestMethod]
        public void DeleteConfirmedView_CheckCorrectlyCountRemaining()
        {
            AddInvoices();

            var controller = new HomeController(testdb);

            var result = controller.DeleteConfirmed(1) as ViewResult;
            IQueryable<InvoiceModel> testQuery = from test in testdb.InvoiceList where test.ID != 1 select test;
            Assert.AreEqual(testQuery.Count(), 2);
        }

        [TestMethod]
        public void InvoiceDetailsView_CheckErrorMessageWhenIDNull()
        {
            var controller = new HomeController(testdb);

            var result = controller.InvoiceDetails(null) as ViewResult;
            Assert.IsTrue(controller.ModelState[""].Errors.Any(modelError => modelError.ErrorMessage == "Invoice ID is Null"));
        }

        [TestMethod]
        public void InvoiceDetailsView_CheckErrorMessageInvalidID()
        {
            var controller = new HomeController(testdb);

            var result = controller.InvoiceDetails(9999) as ViewResult;
            Assert.IsTrue(controller.ModelState[""].Errors.Any(modelError => modelError.ErrorMessage == "No such invoice"));
        }

        [TestMethod]
        public void InvoiceDetailsView_CheckresultCount()
        {
            testdb.InvoiceList.Add(AddDummyInvoice());
            var controller = new HomeController(testdb);

            var result = controller.InvoiceDetails(3) as ViewResult;
            var invoice = (InvoiceModel)result.ViewData.Model;
            Assert.AreEqual(invoice.ID, 3);
        }


        public InvoiceModel AddDummyInvoice()
        {
            return new InvoiceModel() { ID= 3,  Name = "Dummy", TaxPointDate = DateTime.Now, Address = "Dummy Address3", Reference = "6775", TotalGross = 20.25 };
        }

        public void AddInvoices()
        {
            testdb.InvoiceList.Add(new InvoiceModel() { ID=1, Name = "Dummy1", TaxPointDate = DateTime.Now, Address = "Dummy Address", Reference = "1234", TotalGross = 12.25 });
            testdb.InvoiceList.Add(new InvoiceModel() { ID=2, Name = "Dummy2", TaxPointDate = DateTime.Now, Address = "Dummy Address2", Reference = "3545", TotalGross = 10.25 });
            testdb.InvoiceList.Add(new InvoiceModel() { ID=3, Name = "Dummy3", TaxPointDate = DateTime.Now, Address = "Dummy Address3", Reference = "6775", TotalGross = 20.25 });
            
        }
    }
}
