using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Aruna_technicalTest.Models;
using System.Net;
using Aruna_technicalTest.DAL;
using System.Data;

/*
 * Created- Aruna Duraisingam  
 * Date: 22/11/2016
 * Home Controller. All invoice process are implemented here
 */

namespace Aruna_technicalTest.Controllers
{
    public class HomeController : Controller
    {
        private IInvoiceContext db = new InvoiceContext();

        public HomeController() { }

        /*
         * Constructor for handling Depency injection. Unit testing Context will be initialized here.
         */

        public HomeController(IInvoiceContext context)
        {
            db = context;
        }

        /*
          * View to handle Home page. This function also handles the search by name functionality
          */

        public ViewResult Index(string Search)
        {
            var list = from p in db.InvoiceList select p;
            if (!string.IsNullOrWhiteSpace(Search))
            {
                list = list.Where(p => p.Name.Contains(Search));
            }
            return View(list);

           // return View(db.InvoiceList.ToList());
        }

        /*
          * View to create new invoice
          */
        public ViewResult CreateInvoice()
        {
            return View();
        }

        /*
          * Handles the new invoice form posted from client and saves to DB if valid. Else throws exception
          */
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult CreateInvoice([Bind(Include = "TaxPointDate, Reference, TotalGross, Name, Address")]InvoiceModel invoice)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.InvoiceList.Add(invoice);
                    db.SaveChanges();
                    return RedirectToAction("Index");
                }
            }
            catch (DataException)
            {
                ModelState.AddModelError("", "Problem while creating invoice. Try again, and if the problem persists see your system administrator.");
            }
            return View(invoice);
        }

        /*
          * View to render invoice to edit
        */

        public ActionResult EditInvoice(int? id)
        {
            if (id == null)
            {
                ModelState.AddModelError("", "Invoice ID is Null");
            }

            InvoiceModel invoice = getInvoiceByID(id);
            if (invoice == null)
            {
                ModelState.AddModelError("", "No such invoice");
            }

            return View(invoice);
         }

        /*
          * Updates the edited invoice form
        */
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult EditPost(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            var invoicetoUpdate = db.InvoiceList.Find(id);
            if (TryUpdateModel(invoicetoUpdate, "",
               new string[] { "TaxPointDate", "Reference", "TotalGross", "Name", "Address" }))
            {
                try
                {
                    db.SaveChanges();

                    return RedirectToAction("Index");
                }
                catch (DataException)
                {
                    ModelState.AddModelError("", "Problem while saving. If the problem persists, see your system administrator.");
                }
            }
            return View(invoicetoUpdate);
        }

        /*
          * Get the search query and filters by customer name
        */
        [AcceptVerbs(HttpVerbs.Post)]
        public ActionResult SearchInvoice(string Search)
        {

            return View("Index", Search);
        }

        /*
          * View to render Invoice details
        */
        public ViewResult InvoiceDetails(int? id)
        {
            if (id == null)
            {
                ModelState.AddModelError("", "Invoice ID is Null");
            }

            InvoiceModel invoice = getInvoiceByID(id);
            if (invoice == null)
            {
                ModelState.AddModelError("", "No such invoice");
            }

            return View(invoice);
        }

        /*
       * Asynchronous post to delete selected invoice
     */
        [HttpPost, ActionName("DeleteInvoice")]
        public ActionResult DeleteConfirmed(int id)
        {
            InvoiceModel invoice = db.InvoiceList.Find(id);
            db.InvoiceList.Remove(invoice);
            db.SaveChanges();
            return new HttpStatusCodeResult(System.Net.HttpStatusCode.OK);
        }

        /*
        * Function to get invoice deatils by ID
        */

        public InvoiceModel getInvoiceByID (int? id)
        {
            return db.InvoiceList.Find(id);
        }

        protected override void Dispose(bool disposing)
        {
            db.Dispose();
            base.Dispose(disposing);
        }

    }

}