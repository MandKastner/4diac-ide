/*******************************************************************************
 * Copyright (c) 2023 Paul Pavlicek
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Paul Pavlicek
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.contracts.model;

public abstract class AbstractTime {
	// only used to extend from

	public AbstractTime add(final AbstractTime other) {
		if (this instanceof final Instant instant) {
			return instant.add(other);
		}
		if (this instanceof final Interval interval) {
			return interval.add(other);
		}
		return null;
	}

	public int getMin() {
		if (this instanceof final Instant instant) {
			instant.getTime();
		} else if (this instanceof final Interval interval) {
			interval.getMinTime();
		}
		return Integer.MIN_VALUE;
	}

	public int getMax() {
		if (this instanceof final Instant instant) {
			instant.getTime();
		} else if (this instanceof final Interval interval) {
			interval.getMaxTime();
		}
		return Integer.MIN_VALUE;
	}

}
