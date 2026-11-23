document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".panel-alquiler").forEach((panel) => {
        const cardContent = panel.parentElement;
        const toggleButton = cardContent.querySelector("[data-alquilar]");
        const daysContainer = panel.querySelector("[data-calendario-dias]");
        const monthLabel = panel.querySelector("[data-mes-actual]");
        const previousButton = panel.querySelector("[data-mes-anterior]");
        const nextButton = panel.querySelector("[data-mes-siguiente]");
        const message = panel.querySelector("[data-mensaje-fechas]");
        const form = panel.querySelector("[data-formulario-alquiler]");
        const checkInInput = panel.querySelector("[data-fecha-entrada]");
        const checkOutInput = panel.querySelector("[data-fecha-salida]");
        const guestsInput = panel.querySelector("[data-numero-huespedes]");
        const totalLabel = panel.querySelector("[data-precio-total]");

        const propertyId = panel.dataset.propertyId;
        const pricePerNight = Number(panel.dataset.pricePerNight);
        const maxGuests = Number(panel.dataset.guestCapacity);

        let currentMonth = new Date();
        currentMonth.setDate(1);

        let occupiedPeriods = [];
        let checkIn = null;
        let checkOut = null;
        let availabilityLoaded = false;

        guestsInput.max = maxGuests;

        function toDateKey(year, month, day) {
            return `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
        }

        function fromDateKey(dateKey) {
            const [year, month, day] = dateKey.split("-").map(Number);
            return new Date(year, month - 1, day);
        }

        function formatDate(dateKey) {
            return new Intl.DateTimeFormat("es-ES", {
                day: "numeric",
                month: "long",
                year: "numeric"
            }).format(fromDateKey(dateKey));
        }

        function isOccupied(dateKey) {
            return occupiedPeriods.some((period) =>
                dateKey >= period.checkIn && dateKey < period.checkOut
            );
        }

        function rangeOverlapsOccupiedDates(start, end) {
            return occupiedPeriods.some((period) =>
                period.checkIn < end && period.checkOut > start
            );
        }

        function updateTotal() {
            if (!checkIn || !checkOut) {
                return;
            }

            const nights = Math.round(
                (fromDateKey(checkOut) - fromDateKey(checkIn)) / 86400000
            );
            const guests = Number(guestsInput.value);

            if (guests < 1 || guests > maxGuests) {
                totalLabel.textContent = "Revisa el número de huéspedes";
                return;
            }

            const total = pricePerNight * nights;
            totalLabel.textContent = new Intl.NumberFormat("es-ES", {
                style: "currency",
                currency: "EUR"
            }).format(total);
        }

        function renderCalendar() {
            const year = currentMonth.getFullYear();
            const month = currentMonth.getMonth();
            const firstWeekday = (new Date(year, month, 1).getDay() + 6) % 7;
            const daysInMonth = new Date(year, month + 1, 0).getDate();
            const today = new Date();
            const todayKey = toDateKey(
                today.getFullYear(),
                today.getMonth(),
                today.getDate()
            );

            monthLabel.textContent = new Intl.DateTimeFormat("es-ES", {
                month: "long",
                year: "numeric"
            }).format(currentMonth);

            daysContainer.replaceChildren();

            for (let index = 0; index < firstWeekday; index++) {
                const emptyDay = document.createElement("span");
                emptyDay.className = "dia-vacio";
                daysContainer.appendChild(emptyDay);
            }

            for (let day = 1; day <= daysInMonth; day++) {
                const dateKey = toDateKey(year, month, day);
                const dayButton = document.createElement("button");

                dayButton.type = "button";
                dayButton.className = "dia-calendario";
                dayButton.textContent = day;

                if (isOccupied(dateKey)) {
                    dayButton.classList.add("fecha-ocupada");
                    dayButton.disabled = true;
                    dayButton.title = "Fecha ocupada";
                } else if (dateKey < todayKey) {
                    dayButton.disabled = true;
                    dayButton.classList.add("fecha-pasada");
                }

                if (dateKey === checkIn || dateKey === checkOut) {
                    dayButton.classList.add("fecha-seleccionada");
                } else if (checkIn && checkOut
                        && dateKey > checkIn
                        && dateKey < checkOut) {
                    dayButton.classList.add("fecha-en-rango");
                }

                dayButton.addEventListener("click", () => selectDate(dateKey));
                daysContainer.appendChild(dayButton);
            }
        }

        function selectDate(dateKey) {
            if (!checkIn || checkOut) {
                checkIn = dateKey;
                checkOut = null;
                checkInInput.value = "";
                checkOutInput.value = "";
                form.hidden = true;
                message.textContent =
                    `Entrada: ${formatDate(checkIn)}. Ahora elige la salida.`;
                renderCalendar();
                return;
            }

            if (dateKey <= checkIn) {
                checkIn = dateKey;
                message.textContent =
                    `Entrada: ${formatDate(checkIn)}. Ahora elige la salida.`;
                renderCalendar();
                return;
            }

            if (rangeOverlapsOccupiedDates(checkIn, dateKey)) {
                message.textContent =
                    "Ese rango incluye fechas ocupadas. Elige otra salida.";
                return;
            }

            checkOut = dateKey;
            checkInInput.value = checkIn;
            checkOutInput.value = checkOut;
            form.hidden = false;
            message.textContent =
                `Estancia: ${formatDate(checkIn)} – ${formatDate(checkOut)}.`;
            updateTotal();
            renderCalendar();
        }

        async function loadAvailability() {
            message.textContent = "Cargando disponibilidad…";

            try {
                const response = await fetch(
                    `/api/propiedades/${propertyId}/fechas-ocupadas`
                );

                if (!response.ok) {
                    throw new Error("No se pudo cargar la disponibilidad.");
                }

                occupiedPeriods = await response.json();
                availabilityLoaded = true;
                message.textContent = "Elige primero el día de entrada.";
                renderCalendar();
            } catch (error) {
                message.textContent =
                    "No se pudo cargar el calendario. Inténtalo de nuevo.";
            }
        }

        toggleButton.addEventListener("click", async () => {
            panel.hidden = !panel.hidden;
            toggleButton.setAttribute("aria-expanded", String(!panel.hidden));

            if (!panel.hidden && !availabilityLoaded) {
                await loadAvailability();
            }
        });

        previousButton.addEventListener("click", () => {
            currentMonth.setMonth(currentMonth.getMonth() - 1);
            renderCalendar();
        });

        nextButton.addEventListener("click", () => {
            currentMonth.setMonth(currentMonth.getMonth() + 1);
            renderCalendar();
        });

        guestsInput.addEventListener("input", updateTotal);
        renderCalendar();
    });
});